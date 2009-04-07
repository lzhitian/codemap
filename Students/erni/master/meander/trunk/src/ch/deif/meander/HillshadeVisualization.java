package ch.deif.meander;

import processing.core.PGraphics;
import processing.core.PImage;
import ch.akuhn.util.Out;
import ch.deif.meander.Map.Pixel;

public class HillshadeVisualization extends MapVisualization {

    public HillshadeVisualization(Map map) {
        super(map);
    }

    @Override
    public void draw(PGraphics pg) {
        PImage img = new PImage(map.width, map.height);
        this.drawOn(img);
        pg.image(img, 0, 0);
    }

    private MColor color(Pixel p) {
        Parameters params = map.getParameters();
        double elevation = p.elevation();
        if (elevation > params.beachHeight) return new MColor(196, 236, 0);
        if (elevation > params.waterHeight) return new MColor(92, 142, 255);
        return new MColor(0, 68, 255);
    }

    @Override
    public void drawOn(PImage img) {
        assert img.width == map.width && img.height == map.height;
        int[] pixels = img.pixels;
        int index = 0;
        for (Pixel p : map.pixels()) {
            MColor color = color(p);
            if (p.elevation() > map.getParameters().beachHeight) {
                color.darker(p.hillshade());
                if (p.hasContourLine()) color.darker();
            }
            pixels[index++] = color.rgb();
        }
        img.updatePixels();
    }
}
