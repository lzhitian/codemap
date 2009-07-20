package ch.akuhn.org.ggobi.plugins.ggvis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Viz implements PaintListener {

	public double[][] points;

	public Viz(double[][] points) {
		this.points = points;
	}
	
	public Viz open() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final int ms = 1000 / 50;
				final Display display = new Display();
				final Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
				final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
				canvas.addPaintListener(Viz.this);
				canvas.setSize(512, 512);
				shell.setText("MDS");
				shell.pack();
				shell.open();
				display.timerExec(ms , new Runnable() {
					@Override
					public void run() {
						if (shell.isDisposed()) return;
						canvas.redraw();
						display.timerExec(ms, this);
					}
				});
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
				display.dispose();
				System.exit(-1);
			}
		}).start();
		return this;
	}

	@Override
	public void paintControl(PaintEvent e) {
		final double[][] pps = points;
		e.gc.setAntialias(SWT.ON);
		e.gc.setAlpha(128);
		int zoom = 100;
		e.gc.drawOval(256-zoom, 256-zoom, zoom*2, zoom*2);
		for (double[] p: pps) {
			int x = (int) (p[0] * zoom + 256);
			int y = (int) (p[1] * zoom + 256);
			e.gc.drawLine(x - 2, y - 2, x + 2, y + 2);
			e.gc.drawLine(x - 2, y + 2, x + 2, y - 2);
		}
	}
	
}