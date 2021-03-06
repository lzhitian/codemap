package org.codemap.marker;

import static org.eclipse.core.resources.IMarker.MARKER;
import static org.eclipse.core.resources.IMarker.SEVERITY;
import static org.eclipse.core.resources.IResource.DEPTH_INFINITE;

import java.util.HashMap;
import java.util.Map;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.commands.MarkerCommand;
import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class MarkerController {
	
	private static final int NO_SEVERITY = -1;
	
	private IResourceDeltaVisitor resourceDeltaVisitor = new IResourceDeltaVisitor() {

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			if (delta == null) {
				return false;
			}
			if (resourceOpenStateChanged(delta)) {
				handleProjectResourceOpenStateChange(delta.getResource());
				return false;
			}
			for (IMarkerDelta markerDelta: delta.getMarkerDeltas()) {
				switch (markerDelta.getKind()) {
					case IResourceDelta.ADDED :
						handleAddMarker(markerDelta.getMarker());
						break;
					case IResourceDelta.REMOVED :
						handleRemoveMarker(markerDelta.getMarker());
						break;
				}
			}
			return true;
		}

		private boolean resourceOpenStateChanged(IResourceDelta delta) {
			return 0 != (delta.getFlags() & IResourceDelta.OPEN) && 0 == (delta.getFlags() & IResourceDelta.MOVED_FROM);
		}
	};
	
	private IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {
		/*
		 * @see IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
		 */
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta= event.getDelta();
			if (delta == null) return;
			
			try {
				delta.accept(resourceDeltaVisitor);
			} catch (CoreException e) {
				Log.error(e);
			}
		}
	};
	
	private Map<MapPerProject, MarkerSelection> selectionCache = new HashMap<MapPerProject, MarkerSelection>();
    private MarkerCommand currentCommand;

	public MarkerController() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
	}
	
	/**
	 * Loads all markers on the given resource.
	 * 
	 * @param resource the resource which contains the markers
	 */
	private void loadMarkers(IResource resource) {
		try {
			IMarker[] markers = resource.findMarkers(MARKER, true, DEPTH_INFINITE);
			addAllMarkers(collectIdentifiers(markers));
		} catch (CoreException e) {
			Log.error(e);
		}
	}	

	private void addAllMarkers(Map<String, Integer> map) {
		if (! isActive()) return;
		
		getMarkerSelection().addAll(map);
	}

	private Map<String, Integer> collectIdentifiers(IMarker[] markers) {
		Map<String, Integer> identifiers = new HashMap<String, Integer>();
		int severity;
		for(IMarker each: markers) {
			if ((severity = each.getAttribute(SEVERITY, NO_SEVERITY)) == NO_SEVERITY) continue;
			identifiers.put(Resources.asPath(each.getResource()), severity);
		}
		return identifiers;
	}	

	private void removeMarker(IMarker marker) {
		if (! isActive()) return;
		
		String identifier = Resources.asPath(marker.getResource());
		getMarkerSelection().remove(identifier);
	}

	private void addMarker(IMarker marker) {
		if (! isActive()) return;
		int severity;
		if ((severity = marker.getAttribute(SEVERITY, NO_SEVERITY)) == NO_SEVERITY) return;		
		String identifier = Resources.asPath(marker.getResource());
		getMarkerSelection().add(identifier, severity);
	}
	
	private void handleRemoveMarker(IMarker marker) {
		removeMarker(marker);
	}

	private void handleAddMarker(IMarker marker) {
		addMarker(marker);
	}

	/**
	 * A project has been opened or closed.  Updates the breakpoints for
	 * that project
	 */
	private void handleProjectResourceOpenStateChange(IResource project) {
		if (!project.isAccessible()) {
			handleClosedProject(project);
		} else {
			handleOpenedProject(project);
		}
	}

	private void handleOpenedProject(IResource project) {
	    // FIXME: what the fuck this?
		IMarker[] findMarkers;
		try {
			findMarkers = project.findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void handleClosedProject(IResource project) {
	    // FIXME: what the *** is this?
		// TODO: iterate over the list of markers and delete the ones from the closed project
//		Enumeration breakpoints= ((Vector)getBreakpoints0().clone()).elements();
//		while (breakpoints.hasMoreElements()) {
//			IBreakpoint breakpoint= (IBreakpoint) breakpoints.nextElement();
//			IResource markerResource= breakpoint.getMarker().getResource();
//			if (project.getFullPath().isPrefixOf(markerResource.getFullPath())) {
//				fRemoved.add(breakpoint);
//			}
//		}
	}
	
	private boolean isActive() {
		if (currentCommand == null) return false;
		return currentCommand.isEnabled();
	}	

	public void onLayerActivated() {
		loadAllMarkers();
	}	
	
	private void loadAllMarkers() {
		loadMarkers(CodemapCore.getPlugin().getActiveMap().getProject());
	}

	public void onLayerDeactivated() {
		clearSelection();
	}

	private void clearSelection() {
		getMarkerSelection().clear();
	}	

	private MarkerSelection getMarkerSelection() {
        MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
        MarkerSelection selection = selectionCache.get(activeMap);
        if (selection == null) {
            selection = new MarkerSelection();
            selectionCache.put(activeMap, selection);
            MarkersOverlay markersOverlay = new MarkersOverlay();
            markersOverlay.setMarkerSelection(selection);
            activeMap.addSelectionLayer(markersOverlay, selection.getSelection());
        }
        return selection;
	}

    public void setCurrentCommand(MarkerCommand markerCommand) {
        currentCommand = markerCommand;
    }	
}
