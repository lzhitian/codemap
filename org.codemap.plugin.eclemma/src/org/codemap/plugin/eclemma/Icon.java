package org.codemap.plugin.eclemma;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

public class Icon {

	public static final String COVERAGE_OVERLAY = "icons/eclipse/category.gif";

	public static ImageDescriptor getImageDescriptor(String key) {
		return loadImage(key).getDescriptor(key);
	}

	private static ImageRegistry loadImage(String path) {
		ImageRegistry reg = EclemmaOverlay.getDefault().getImageRegistry();
		if (reg.getDescriptor(path) == null) {
			URL url = EclemmaOverlay.getDefault().getBundle().getEntry(path);
			reg.put(path, ImageDescriptor.createFromURL(url));
		}
		return reg;
	}

}