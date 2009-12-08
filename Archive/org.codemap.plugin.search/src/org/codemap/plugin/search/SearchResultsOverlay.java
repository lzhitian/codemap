package org.codemap.plugin.search;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SelectionOverlay;

public class SearchResultsOverlay extends SelectionOverlay {

    protected final int SELECTION_SIZE = 15;
    protected final int SELECTION_STROKE = 3;

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        // draw background
        gc.fillOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
                SELECTION_SIZE, SELECTION_SIZE);
        // draw stroke
        gc.drawOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
                SELECTION_SIZE, SELECTION_SIZE);		
    }

    @Override
    public void paintBefore(MapValues map, GC gc) {
        Device device = gc.getDevice();
        gc.setForeground(device.getSystemColor(SWT.COLOR_BLUE));
        gc.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
        gc.setAlpha(255);
        gc.setLineWidth(SELECTION_STROKE);
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return SearchPlugin.getPlugin().getSearchSelection();
    }


}