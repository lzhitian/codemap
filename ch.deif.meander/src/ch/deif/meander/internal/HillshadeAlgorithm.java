package ch.deif.meander.internal;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import ch.deif.meander.MapAlgorithm;
import ch.deif.meander.MapSetting;
import ch.deif.meander.util.StopWatch;

/**
 * @see http://edndoc.esri.com/arcobjects/9.2/NET/shared/geoprocessing/ spatial_analyst_tools/how_hillshade_works.htm
 * @see Burrough, P. A. and McDonell, R.A., 1998. Principles of Geographical Information Systems (Oxford University
 *      Press, New York), p. 190.
 */
public class HillshadeAlgorithm extends MapAlgorithm<double[][]> {

    private static final double Z_FACTOR = 0.6e-3;
    private static final double CONTOUR_DARKEN_FACTOR = 0.5;
    public static final MapSetting<Integer> CONTOUR_STEP = MapSetting.define("CONTOUR_STEP", 10);

    @Override
    public double[][] call() {
        StopWatch stopWatch = new StopWatch("HillShade").start();
        
        // zenith: height of sun over horizon (90 = horizon, 0 = zenith).
        double zenithRad = 45 * PI / 180;
        // azimuth: direction of sun on x-y-plane
        double azimuthRad = (315 - 180) * PI / 180;
        double z_factor = Z_FACTOR * map.getWidth();
        
        int step = map.get(CONTOUR_STEP);
        
        double[][] hillshade = new double[map.width][map.width];
        float[][] DEM = map.getDEM();
        
        for (int x = 1; x < DEM.length-2; x++) {
            for (int y = 1; y < DEM[0].length-2; y++) {
                float here = DEM[x][y];
                if (here == 0.0) continue;
                
                // build kernel
                int yTop = y - 1;
                int xLeft = x - 1;
                int yBottom = y + 1;
                int xRight = x + 1;

                float topLeft = DEM[xLeft][yTop];
                float top = DEM[x][yTop];
                float topRight = DEM[xRight][yTop];
                float left = DEM[xLeft][y];
                float right = DEM[xRight][y];
                float bottomLeft = DEM[xLeft][yBottom];
                float bottom = DEM[x][yBottom];
                float bottomRight = DEM[xRight][yBottom];
                
                // calculate hillshading
                double dx = (topRight + (2 * right) + bottomRight - (topLeft + (2 * left) + bottomLeft)) / 8;
                double dy = (bottomLeft + (2 * bottom) + bottomRight - (topLeft + (2 * top) + topRight)) / 8;
                double slopeRad = atan(z_factor * sqrt(dx * dx + (dy * dy)));
                double aspectRad = atan2(dy, -dx);
                double shading = (cos(zenithRad) * cos(slopeRad) + (sin(zenithRad) * sin(slopeRad) * cos(azimuthRad
                        - aspectRad)));
                hillshade[x][y] = shading;
                
                // calculate contourlines
                int topHeight = (int) Math.floor(top / step);
                int leftHeight = (int) Math.floor(left / step);
                int hereHeight = (int) Math.floor(here / step);
                boolean isCoastline = topHeight == 0 || leftHeight == 0 || hereHeight == 0;
                boolean isContourLine = (topHeight != hereHeight || leftHeight != hereHeight) && !isCoastline;
                if (isContourLine) {
                    hillshade[x][y] *= CONTOUR_DARKEN_FACTOR;
                }
            }
        }
        stopWatch.printStop();
        return hillshade;
    }
}
