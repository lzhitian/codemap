package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PACKAGES;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.MapController;
import org.codemap.util.CodemapColors;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ColorBrewer;
import org.codemap.util.Log;
import org.codemap.util.MColor;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import ch.deif.meander.Point;

public class ShowPackageColorsAction extends MenuAction {
	
	private final MapController theController;

	public ShowPackageColorsAction(MapController controller, int style) {
		super("Color by Package", style);
		this.theController = controller;
		setChecked(isDefaultChecked());
		setImageDescriptor(CodemapIcons.descriptor(PACKAGES));
	}

	@Override
	public void run() {
		super.run();
		
		if (isChecked()) {
			enable();
		} else {
			disable();
		}
	}

	private void disable() {
	    CodemapCore core = CodemapCore.getPlugin();
	    core.getActiveMap().getValues().colorScheme.setValue(core.getDefaultColorScheme());
	}

	private void enable() {
		MapPerProject mapForProject = CodemapCore.getPlugin().mapForProject(theController.getCurrentProject());
		
		ColorBrewer brewer = new ColorBrewer();
		CodemapColors colorScheme = new CodemapColors();
		
		for(Point each: mapForProject.getConfiguration().points()) {
			
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
		CodemapCore.getPlugin().getActiveMap().getValues().colorScheme.setValue(colorScheme);
	}

	@Override
	protected String getKey() {
		return "show_package_colors";
	}

	@Override
	protected boolean isDefaultChecked() {
		return false;
	}

}
