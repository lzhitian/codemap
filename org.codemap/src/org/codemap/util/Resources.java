package org.codemap.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import ch.akuhn.util.Files;

public class Resources {

	public static String asPath(IResource resource) {
		return resource.getFullPath().toString();
	}
	
	public static IJavaElement asJavaElement(String path) {
		IResource resource = asResource(path);
		return JavaCore.create(resource);
	}
	
	public static IResource asResource(String path) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.findMember(new Path(path));
	}
	
	public static IResource fromJavaElement(IJavaElement element) {
		return element.getResource();
	}
	
	public static boolean matchPattern(IResource respource, String pattern) {
		String name = respource.getName();
		return Files.match(pattern, name);
	}
	
	public static IContainer getFolder(IResource resource) {
		if (resource instanceof IContainer) return (IContainer) resource;
		return resource.getParent();
	}

	public static String asPath(IJavaElement element) {
		return asPath(element.getResource());
	}

	public static String asPath(Object each) {
		IResource resource = Adaptables.adapt(each, IResource.class);
		return asPath(resource);
	}
	
}
