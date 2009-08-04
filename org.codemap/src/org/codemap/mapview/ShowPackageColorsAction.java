package org.codemap.mapview;

import static org.codemap.util.Icons.PACKAGES;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.util.CodemapColors;
import org.codemap.util.ColorBrewer;
import org.codemap.util.Icons;
import org.codemap.util.Log;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;

import ch.deif.meander.Point;
import ch.deif.meander.util.MColor;

public class ShowPackageColorsAction extends CodemapAction {
	
	private static final boolean DEFAULT_VALUE = false;
	private static final String KEY = "show_package_colors";
	private final MapController theController;

	public ShowPackageColorsAction(MapController controller, int style) {
		super("Color by Package", style);
		this.theController = controller;
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(PACKAGES));
	}

	@Override
	public void run() {
		CodemapCore.getPlugin().getActiveMap().setProperty(KEY, isChecked());		
		if (isChecked()) {
			enable();
		} else {
			disable();
		}
	}

	private void disable() {
		getColorScheme().clearColors();
		redraw();
	}

	private CodemapColors getColorScheme() {
		return CodemapCore.getPlugin().getColorScheme();
	}

	private void enable() {
		MapPerProject mapForProject = CodemapCore.getPlugin().mapForProject(theController.getCurrentProject());
		
		ColorBrewer brewer = new ColorBrewer();
		CodemapColors colorScheme = getColorScheme();
		
		for(Point each: mapForProject.getConfiguration().points()) {
//			System.out.println(each.getDocument());
			
			String fileName = each.getDocument();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(new Path(fileName));
			IJavaElement create = JavaCore.create(resource);
			
			if (!(create instanceof ICompilationUnit)) return;
			ICompilationUnit unit = (ICompilationUnit) create;
			try {
				IPackageDeclaration[] packageDeclarations = unit.getPackageDeclarations();
				if (packageDeclarations.length != 1) return;
				
				IPackageDeclaration pack = packageDeclarations[0];
				String packageName = pack.getElementName();
				MColor color = brewer.forPackage(packageName);
				colorScheme.setColor(fileName, color);

			} catch (JavaModelException e) {
				Log.error(e);
			}
		}
		redraw();
	}

	private void redraw() {
		CodemapCore.getPlugin().redrawCodemapBackground();
	}

	@Override
	public void configureAction(MapPerProject map) {
		setChecked(map.getPropertyOrDefault(KEY, DEFAULT_VALUE));
	}
	

}
