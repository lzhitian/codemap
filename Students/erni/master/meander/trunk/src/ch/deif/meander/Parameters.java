package ch.deif.meander;

public class Parameters {

    public enum ColorScheme {
        NONE, GREEN, NEAREST_NEIGHBOUR, FILE_KIND, PACKAGE;
    }
    
    public ColorScheme colorScheme = ColorScheme.GREEN;
    
    public int waterHeight = 6;
    public int contourLineStep = 10;
    
    public int numberOfLabels = 12;
    public int labelSize = 32;
    
    public boolean showWater = true;
    public boolean showHillshade = true;
    public boolean showContourLines = true;
    public boolean showColors = true;
    public boolean showLabels = false;

    public int width = 512;
    public int height = 512;


    public void useExample() {
        showContourLines = false;
        showHillshade = false;
        showWater = false;
        showColors = false;
        showLabels = false;
        height = width = 640;
    }

}
