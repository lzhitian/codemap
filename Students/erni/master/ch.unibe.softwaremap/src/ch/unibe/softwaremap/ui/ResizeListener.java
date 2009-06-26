package ch.unibe.softwaremap.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ch.deif.meander.ui.EclipseProcessingBridge;
import ch.unibe.softwaremap.CodemapCore;

public class ResizeListener implements ControlListener {
	
	private Composite eventsource;
	private EclipseProcessingBridge target;
	private boolean mouseDown;
	private boolean resized;
	private MapController theController;

	public ResizeListener(Composite eventsource, EclipseProcessingBridge softwareMap, MapController theController) {
		this.eventsource = eventsource;
		this.target = softwareMap;
		this.theController = theController;
		setupListeners();
	}

	private void setupListeners() {
		eventsource.addControlListener(this);
		
		Display display = Display.getCurrent();
		addMouseUpListener(display);
		addMouseDownListener(display);			
	}

	private void addMouseUpListener(Display display) {
		display.addFilter(SWT.MouseDown, new Listener(){
			@Override
			public void handleEvent(Event event) {
				mouseDown();
			}
		});
	}

	private void addMouseDownListener(Display display) {
		display.addFilter(SWT.MouseUp, new Listener(){
			@Override
			public void handleEvent(Event event) {
				mouseUp();
			}
		});
	}
	
	private void mouseUp() {
		mouseDown = false;
		if (resized) {
			resized = false;
			processResize();
		}
	}	
	
	private void mouseDown() {
		mouseDown = true;
	}

	@Override
	public void controlResized(ControlEvent e) {
		if (! mouseDown) {
			processResize();
		}
		resized = true;
	}

	private void processResize() {
		theController.onResize();
//		System.out.println("resize the map");
		Point size = target.getSize();
		CodemapCore.getPlugin().updateMapdimension(Math.min(size.x, size.y));
	}

	@Override
	public void controlMoved(ControlEvent e) {
		// don't care
	}


}
