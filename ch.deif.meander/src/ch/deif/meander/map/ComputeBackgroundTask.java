package ch.deif.meander.map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;
import ch.deif.meander.util.StopWatch;

public class ComputeBackgroundTask extends TaskValue<Image> {

    public ComputeBackgroundTask(
            Value<MapInstance> mapInstance, 
            Value<DigitalElevationModel>  elevationModel, 
            Value<HillShading> shading, 
            Value<MapScheme<MColor>> colors) {
        super("Caching background image", mapInstance, elevationModel, shading, colors);
        this.doesNotRequireAllArguments(); 
    }

    @Override
    protected Image computeValue(ProgressMonitor monitor, Arguments arguments) {
        MapInstance mapInstance = arguments.nextOrNull();
        if (mapInstance == null) return null;

        DigitalElevationModel elevationModel = getCurrentElevationModel(arguments, mapInstance);
        HillShading hillShading = getCurrentHillShading(arguments, mapInstance);
        MapScheme<MColor> colors = getCurrentColorScheme(arguments);
        return computeValue(monitor, mapInstance, elevationModel, hillShading, colors);
    }

    private Image computeValue(ProgressMonitor monitor, MapInstance mapInstance, DigitalElevationModel elevationModel, HillShading hillShading, MapScheme<MColor> colors) {
        int mapSize = mapInstance.getWidth();
        Device device = Display.getCurrent();
        Image image = new Image(device, mapSize, mapSize);
        GC gc = new GC(image);
        this.paintWater(monitor, gc);        
        this.paintBackground(monitor, gc, mapInstance, elevationModel, hillShading, colors);
        gc.dispose();
        return image;
    }

    private MapScheme<MColor> getCurrentColorScheme(Arguments arguments) {
        MapScheme<MColor> colors = arguments.nextOrNull();
        if (colors == null) colors = MapScheme.with(MColor.HILLGREEN);
        return colors;
    }

    private DigitalElevationModel getCurrentElevationModel(Arguments arguments, MapInstance mapInstance) {
        DigitalElevationModel elevationModel = arguments.nextOrNull();
        if (elevationModel != null && mapInstance.getWidth() != elevationModel.getSize()) 
            elevationModel = null;
        return elevationModel;
    }

    private HillShading getCurrentHillShading(Arguments arguments, MapInstance mapInstance) {
        HillShading hillShading = arguments.nextOrNull();
        if (hillShading != null && mapInstance.getWidth() != hillShading.getSize())
            hillShading = null;
        return hillShading;
    }

    private void paintBackground(ProgressMonitor monitor, GC gc, MapInstance mapInstance, DigitalElevationModel elevationModel, HillShading hillShading, MapScheme<MColor> colors) {
        if (elevationModel == null) {
            StopWatch stopWatch = new StopWatch("Background (Draft)").start();
            paintDraft(monitor, gc, mapInstance);
            stopWatch.printStop();
        }
        else {
            StopWatch stopWatch = new StopWatch("Background").start();
//            paintShores(monitor, gc, mapInstance, elevationModel);
            paintHills(monitor, gc, mapInstance, elevationModel, hillShading, colors);
            stopWatch.printStop();
        }
    }

    private void paintWater(ProgressMonitor monitor, GC gc) {
            if (monitor.isCanceled()) return;
            Color blue = new Color(gc.getDevice(), 0, 0, 255);
            gc.setBackground(blue);
            gc.fillRectangle(gc.getClipping());
            blue.dispose();
    }

    private void paintDraft(ProgressMonitor monitor, GC gc, MapInstance map) {
        if (monitor.isCanceled()) return;
        if (map == null) return;
        
        Color color = new Color(gc.getDevice(), 92, 142, 255);
        gc.setForeground(color);
        gc.setLineWidth(2);
        for (Location each: map.locations()) {
            if (monitor.isCanceled()) break;
            int r = (int) (each.getElevation() * 2 * map.getWidth() / DEMAlgorithm.MAGIC_VALUE);
            gc.drawOval(each.px - r, each.py - r, r * 2, r * 2);
        }
        color.dispose();
    }

    private void paintHills(ProgressMonitor monitor, GC gc, MapInstance mapInstance, DigitalElevationModel elevationModel, HillShading hillShading, MapScheme<MColor> colors) {
        if (monitor.isCanceled()) return;
        if (hillShading == null) return;
                
        int mapSize = mapInstance.getWidth();
        float[][] DEM = elevationModel.asFloatArray();
        double[][] shade = hillShading.asDoubleArray();
        
        // 1 byte per color, we fill 24bit per pixel
        byte[] imageBytes = new byte[mapSize*mapSize*3];
        byte[] alphaBytes = new byte[mapSize*mapSize];
        
        // colors needed later
        byte[] waterColor = new byte[]{(byte) 255, (byte) 0, (byte) 0};
        byte[] shoreColor = new byte[]{(byte) -1, (byte) 142, (byte) 92};
        
        // set black background so that the transparency works
        // getting it via SWT.COLOR_BLACK raises invalid Thread access
        // and it don't want to fire up a runnable here ...
        Color black = new Color(gc.getDevice(), 0, 0, 0);
        gc.setBackground(black);
        gc.fillRectangle(gc.getClipping());
        black.dispose();    
        
        for(int i=0; i < mapSize*mapSize; i++) {
            int x = i / mapSize;
            int y = i % mapSize;
            
            if (x == 0) {
                // check for cancellation at every new row
                if (monitor.isCanceled()) return;                
            }
            
            if (DEM[x][y] <= 2) {
                // water color
                System.arraycopy(waterColor, 0, imageBytes, i*3, 3);
                alphaBytes[i] = (byte) 255;
                continue;
            } else if (DEM[x][y] <= 10) {
                // shore colors
                System.arraycopy(shoreColor, 0, imageBytes, i*3, 3);
                alphaBytes[i] = (byte) 255;
                continue;
            }
            // get color from location a.k.a. hill colors
            double f = shade[x][y];            
            MColor mcolor = colors.forLocation(mapInstance.nearestNeighbor(x, y).getPoint());
            // make rgb
            int baseIndex = i*3;
            // -1 = 0xFF as the stuff is signed all the time. uehh.
            imageBytes[baseIndex++] = (byte) mcolor.getBlue(); // B
            imageBytes[baseIndex++] = (byte) mcolor.getGreen(); // G
            imageBytes[baseIndex++] = (byte) mcolor.getRed(); // R
            
            // make alpha
            // 0 for fully transparent
            // 255 for fully opaque
            assert f <=1;
            // alpha values got negative somehow so we fix that
            int alpha = (int) Math.max(0.0, 255*f);
            alphaBytes[i] = (byte) alpha;
        }
        // define a direct palette with masks for RGB
        PaletteData palette = new PaletteData(0xFF , 0xFF00 , 0xFF0000);   
        ImageData imageData = new ImageData(mapSize, mapSize, 24, palette, mapSize*3, imageBytes);
        // enable alpha by setting alphabytes ... strange that i can't do that per pixel using 32bit values
        imageData.alphaData = alphaBytes;
        gc.drawImage(new Image(gc.getDevice(), imageData), 0, 0);
    }

    @Override
    protected void maybeDisposeValue(Image previous, Image newValue) {
        if (previous != null && !previous.equals(newValue)) previous.dispose();
    }
    
}
