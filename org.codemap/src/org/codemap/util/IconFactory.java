package org.codemap.util;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public abstract class IconFactory {

	protected ImageDescriptor getImageDescriptor(String key) {
		return loadImage(key).getDescriptor(key);
	}

	protected ImageRegistry loadImage(String path) {
		ImageRegistry reg = getActivator().getImageRegistry();
		if (reg.getDescriptor(path) == null) {
			URL url = getActivator().getBundle().getEntry(path);
			reg.put(path, ImageDescriptor.createFromURL(url));
		}
		return reg;
	}

	protected abstract AbstractUIPlugin getActivator();

}
