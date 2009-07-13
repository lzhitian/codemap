package org.codemap;

import java.util.HashMap;
import java.util.Map;

import org.codemap.builder.HapaxBuilder;
import org.codemap.builder.MapMakerBackgroundJob;
import org.codemap.mapview.MapView;
import org.codemap.util.Log;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.osgi.service.prefs.BackingStoreException;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Pair;
import ch.deif.meander.Configuration;
import ch.deif.meander.Point;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.visual.CurrentSelectionOverlay;
import ch.deif.meander.visual.Layer;
import ch.deif.meander.visual.MapVisualization;
import ch.deif.meander.visual.OpenFilesOverlay;
import ch.deif.meander.visual.YouAreHereOverlay;
import ch.unibe.scg.util.Extension;

/**
 * Holds corpus, map and visualization of a project. Use this class to store project specific information.
 * 
 */
public class MapPerProject {

	private static final String POINT_NODE_ID = CodemapCore.PLUGIN_ID + ".points"; 
	private static final int MINIMAL_SIZE = 300;

	private final IJavaProject project;
	private TermDocumentMatrix tdm;
	private MapVisualization mapViz;
	private boolean mapBeingCalculated = false;
	private boolean builderIsRunning = false;
	private int mapSize = MINIMAL_SIZE;


	private Hapax hapax;

	private Configuration configuration;
	private Layer layer;

	public MapPerProject(IJavaProject project) {
		this.project = project;
	}

	public MapPerProject enableBuilder() {
		try {
			addBuilderToProjectDescriptionCommands();
			if (tdm == null) {
				makeBuilderBackgroundJob().schedule();
			}
		} catch (CoreException e) {
			Log.error(e);
		}
		return this;
	}

	private Job makeBuilderBackgroundJob() {
		return new Job("Initial build of Hapax vocabulary.") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (builderIsRunning) return Status.OK_STATUS;
				try {
					builderIsRunning = true;
					if (getJavaProject() != null) {
						getProject().build(IncrementalProjectBuilder.FULL_BUILD, HapaxBuilder.BUILDER_ID, null, monitor);
					}
				} catch (CoreException e) {
					Log.error(e);
				} finally {
					builderIsRunning = false;
				}
				return Status.OK_STATUS;
			}
		};
	}

	private void addBuilderToProjectDescriptionCommands() throws CoreException {
		if (getJavaProject() == null || !getJavaProject().isOpen()) return;

		IProjectDescription desc = getProject().getDescription();
		ICommand[] commands = desc.getBuildSpec();
		for (ICommand command: commands) {
			if (command.getBuilderName().equals(HapaxBuilder.BUILDER_ID)) {
				return;
			}
		}
		ICommand newCommand = desc.newCommand();
		newCommand.setBuilderName(HapaxBuilder.BUILDER_ID);
		commands = Extension.$(commands).copyWith(newCommand);
		desc.setBuildSpec(commands);
		getProject().setDescription(desc, null);
	}

	public void putTDM(TermDocumentMatrix tdm) {
		this.tdm = tdm;
		this.startBackgroundTask();
	}

	private void startBackgroundTask() {
		new MapMakerBackgroundJob(MapPerProject.this).schedule();
	}

	public MapVisualization getVisualization() {
		if (tdm == null) return null;
		if (mapViz != null && mapViz.getWidth() == mapSize) return mapViz;
		updateMap();
		return null;
	}

	public void updateMap() {
		if (mapBeingCalculated) return;
		mapViz.redraw();
		startBackgroundTask();
	}
	
	public IProject getProject() {
		return getJavaProject().getProject();
	}

	private IJavaProject getJavaProject() {
		return project;
	}

	public IStatus makeMap(IProgressMonitor monitor) {
		if (mapBeingCalculated) return Status.OK_STATUS;
		mapBeingCalculated = true;
		monitor.beginTask("Making map", 50);
		if (hapax == null) {
			hapax = Hapax.legomenon()
					.addCorpus(tdm)
					.closeCorpus()
					.createIndex();
			monitor.worked(10);
			configuration = Meander.builder()
					.addCorpus(hapax)
					.makeMap(reloadMapState());
			monitor.worked(5);
			layer = Meander.visualization()
					.withLabels(CodemapCore.getPlugin().getLabelScheme())
					.withColors(CodemapCore.getPlugin().getColorScheme())
					.withSelection(new CurrentSelectionOverlay(), CodemapCore.getPlugin().getCurrentSelection())
					.withSelection(new OpenFilesOverlay(), CodemapCore.getPlugin().getOpenFilesSelection())
					.withSelection(new YouAreHereOverlay(), CodemapCore.getPlugin().getYouAreHereSelection())
					.appendLayer(CodemapCore.getPlugin().getSharedLayer())
					.makeLayer();
			monitor.worked(5);
		} else {
			monitor.worked(20);
		}

		mapViz = new MapVisualization(configuration.withSize(mapSize), layer);
		monitor.worked(20);

		notifyMapView();
		monitor.done();
		mapBeingCalculated = false;
		return Status.OK_STATUS;
	}

	private Map<String, Pair<Double,Double>> reloadMapState() {
		IEclipsePreferences node = getPointNode();
		Map<String, Pair<Double,Double>> points = new HashMap<String,Pair<Double,Double>>();
		try {
			for(String key: node.keys()) {
				String pointString = node.get(key, null);
				String[] split = pointString.split("#");
				if (split.length != 2 ) {
					Log.error(new RuntimeException("Invalid format of point storage for " + getProject().getName() + ": " + pointString));
					continue;
				}
				double x = Double.parseDouble(split[0]);
				double y = Double.parseDouble(split[1]);
				points.put(key, new Pair<Double, Double>(x, y));
			}
			return points;
		} catch (BackingStoreException e) {
			Log.error(e);
		}
		return null;
	}

	private void notifyMapView() {
		MapView mapView = CodemapCore.getPlugin().getMapView();
		if (mapView != null) {
			mapView.newProjectMapAvailable(project);
		}
	}

	public MapPerProject updateSize(int newMapDimension) {
		this.mapSize = Math.max(newMapDimension, MINIMAL_SIZE);
		return this;
	}

	public void saveMapState() {
		IEclipsePreferences node = getPointNode();
		for(Point each: configuration.points()) {
			node.put(each.getDocument().getIdentifier(), each.x + " # " + each.y);
		}
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Log.error(e);
		}		
	}

	private IEclipsePreferences getPointNode() {
		IScopeContext context = new ProjectScope(getProject());
		IEclipsePreferences node = context.getNode(POINT_NODE_ID);
		return node;
	}

}
