package ch.deif.meander.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import ch.deif.meander.Location;
import ch.deif.meander.map.MapValues;

public abstract class ImageOverlay extends SelectionOverlay {

    private Image image;
    private int offset_x;
    private int offset_y;

    public ImageOverlay(Image img) {
        image = img;
        Rectangle offset = image.getBounds();		
        offset_x = offset.width/2;
        offset_y = offset.height/2;
    }

    @Override
    public void paintBefore(MapValues map, GC gc) {
        super.paintBefore(map, gc);
        gc.setAlpha(255);
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        gc.drawImage(image, each.px - offset_x, each.py - offset_y);
    }

}
