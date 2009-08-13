package org.codemap.mapview;

import java.util.ArrayList;
import java.util.Collection;

import org.codemap.CodemapCore;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPart;

import ch.deif.meander.MapSelection;

public class EditorPartListener implements IPartListener {

	private MapSelection editorSelection;
	private MapSelection youAreHereSelection;

	public EditorPartListener(MapSelection editorSelection, MapSelection youAreHereSelection) {
		this.editorSelection = editorSelection;
		this.youAreHereSelection = youAreHereSelection;
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		// not related to our interests
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		updateEditorSelection(part);
		updateYouAreHereSelection(part);
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		updateEditorSelection(part);
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// not related to our interests
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		updateEditorSelection(part);
	}

	private void updateEditorSelection(IWorkbenchPart part) {
		if (!(part instanceof IEditorPart)) return;
		Collection<String> selection = new ArrayList<String>();
		for (IEditorReference each: part.getSite().getPage().getEditorReferences()) {
            for (IFile file: getFiles(each.getPart(false))) {
                selection.add(Resources.asPath(file));
            }
		}
		editorSelection.replaceWith(selection);
		if (selection.isEmpty()) youAreHereSelection.clear();
		CodemapCore.getPlugin().redrawCodemap();
	}
	
	@SuppressWarnings("deprecation")
	private IFile[] getFiles(IWorkbenchPart each) {
		if (!(each instanceof IEditorPart)) return new IFile[] {};
		IEditorInput input = ((IEditorPart) each).getEditorInput();
		if (!(input instanceof IPathEditorInput)) return new IFile[] {};
		IPathEditorInput pathInput = (IPathEditorInput) input;
//		System.out.println(pathInput.getPath());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot().findFilesForLocation(pathInput.getPath());
	}

	private void updateYouAreHereSelection(IWorkbenchPart part) {
		Collection<String> selection = new ArrayList<String>();
		for (IFile file: getFiles(part)) {
			selection.add(Resources.asPath(file));
		}
		youAreHereSelection.replaceWith(selection);
		CodemapCore.getPlugin().redrawCodemap();
	}

}
