package org.codemap.layers;

import static org.codemap.layers.CodemapVisualization.mapValues;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import org.codemap.CodemapCore;
import org.codemap.Location;
import org.codemap.MapInstance;
import org.codemap.MapSelection;
import org.codemap.internal.DEMAlgorithm;
import org.codemap.mapview.MapSelectionProvider;
import org.codemap.resources.MapValues;
import org.codemap.util.EclipseUtil;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import ch.akuhn.util.List;
import ch.akuhn.values.Value;


public class CurrentSelectionOverlay extends SelectionOverlay {

    protected final int POINT_STROKE = 2;

    public static final String EVT_DOUBLE_CLICKED = "doubleClicked";
    public static final String EVT_SELECTION_CHANGED = "selectionChanged";

    private boolean isDragging = false;
    private Point dragStart;
    private Point dragStop;

    @Override
    public void dragDetected(DragDetectEvent e) {
        isDragging = true;
        dragStop = dragStart = new Point(e.x, e.y);	
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        MapInstance map = CodemapVisualization.mapValues(e).mapInstance.getValue();
        if (map == null) return;
        Location neighbor = map.nearestNeighbor(e.x, e.y);
        if (neighbor == null) return;
        
        IResource resource = Resources.asResource(neighbor.getDocument());
        if (!(resource instanceof IFile))
            return;
        EclipseUtil.openInEditor((IFile) resource);        
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (!isDragging) return;
        dragStop = new Point(e.x, e.y);	
        this.redraw(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (isDragging) {
            isDragging = false;
            updateSelection(mapValues(e));            
        }
        else if (e.count == 1) {
            handleSingleClick(e);            
        }
        this.redraw(e);
        selectionChanged(getSelection(mapValues(e)));
    }
    
    public void selectionChanged(MapSelection mapSelection) {
        final ArrayList<IJavaElement> selection = new ArrayList<IJavaElement>();
        for (String each : mapSelection) {
            IJavaElement javaElement = Resources.asJavaElement(each);
            selection.add(javaElement);
        }
        StructuredSelection structuredSelection = new StructuredSelection(selection);
        MapSelectionProvider selectionProvider = CodemapCore.getPlugin().getController().getSelectionProvider();
        selectionProvider.setSelection(structuredSelection);
    }    

    private void handleSingleClick(MouseEvent e) {
        // only update selection for clicks with primary mouse button
        if (e.button != 1) return;
        
        Value<MapInstance> mapInstance = mapValues(e).mapInstance;
        Location neighbor = mapInstance.getValueOrFail().nearestNeighbor(e.x, e.y);
        if (neighbor == null) return;
        if ((e.stateMask & SWT.SHIFT) != 0 || (e.stateMask & SWT.CTRL) != 0) {
            handleMultipleSelect(neighbor, getSelection(mapValues(e)));
        }
        else {
            handleSingleSelect(neighbor, getSelection(mapValues(e)));
        }
    }

    private void handleMultipleSelect(Location neighbor, MapSelection mapSelection) {
        mapSelection.add(neighbor.getDocument());
    }

    private void handleSingleSelect(Location neighbor, MapSelection mapSelection) {
        mapSelection.replaceAll(List.of(neighbor.getDocument()));
    }

    @Override
    public void paintBefore(MapValues map, GC gc) {
        gc.setLineWidth(POINT_STROKE);
        if (isDragging) {
            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_LIST_SELECTION));
            
            updateSelection(map);
            int deltaX = dragStop.x - dragStart.x;
            int deltaY = dragStop.y - dragStart.y;
            gc.drawRectangle(dragStart.x, dragStart.y, deltaX, deltaY);
        }
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        Device device = gc.getDevice();
        int mapSize = map.mapSize.getValue();
        int magic_r = (int) (each.getElevation() * 2 * mapSize / DEMAlgorithm.MAGIC_VALUE);
        
        gc.setAlpha(128);        
        gc.setBackground(device.getSystemColor(SWT.COLOR_LIST_SELECTION));
        gc.fillOval(each.px - magic_r, each.py - magic_r, magic_r * 2, magic_r * 2);
        
        gc.setAlpha(255);        
        gc.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
        
        gc.drawOval(each.px-magic_r, each.py-magic_r, magic_r*2, magic_r*2);
    }
    
    private void updateSelection(MapValues map) {
        MapInstance mapInstance = map.mapInstance.getValue();
        if (mapInstance == null) return;
        int minX = Math.min(dragStart.x, dragStop.x);
        int minY = Math.min(dragStart.y, dragStop.y);
        int maxX = Math.max(dragStart.x, dragStop.x);
        int maxY = Math.max(dragStart.y, dragStop.y);		
        Collection<String> ids = new ArrayList<String>();
        for (Location each : mapInstance.locations()) {
            if (each.px < maxX && each.px > minX && each.py < maxY && each.py > minY) {			
                ids.add(each.getDocument());
            }
        }
        getSelection(map).replaceAll(ids);
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return map.currentSelection;
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (!(e.keyCode == SWT.ESC)) return;
        MapSelection selection = getSelection(CodemapCore.getPlugin().getActiveMap().getValues());
        selection.clear();
        selectionChanged(selection);
    }

}
