package ch.deif.meander;

import java.util.Arrays;
import java.util.Collection;

import ch.akuhn.util.Providable;

public class Map {

    private double[][] DEM;
    private double[][] hillshade;
    private boolean[][] contours;
    public final int width, height;
    public Collection<Location> locations;
    private Parameters parameters;
    public String name;
    
    public Map(Parameters parameters, Location... locations) {
        this(parameters, Arrays.asList(locations));
    }

    public Map(Parameters parameters, Collection<Location> locations) {
        this.parameters = parameters;
        this.locations = locations;
        width = parameters.width;
        height = parameters.height;
    }
    
    private double[][] getDEM() {
        return DEM == null ? DEM = new double[width][height] : DEM;
    }

    private boolean[][] getContours() {
        return contours == null ? contours = new boolean[width][height] : contours;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Iterable<Location> locations() {
        return locations;
    }
    
    public MapVisualization createVisualization() {
        return new SketchVisualization(this);
    }
    
    public Pixel get(int x, int y) {
        return new Pixel(x, y);
    }

    public Iterable<Pixel> pixels() {
        return pixelsByRows();
    }
    
    public Iterable<Pixel> pixelsByRows() {
        return new Providable<Pixel>() {
            private int x, y;

            @Override
            public void initialize() {
                x = y = 0;
            }

            @Override
            public Pixel provide() {
                if (x >= width) {
                    x = 0;
                    y++;
                }
                if (y >= height) return done();
                Pixel p = new Pixel(x++, y);
                return p;
            }
        };
    }
 
    public Iterable<Pixel> pixelsByColumns() {
        return new Providable<Pixel>() {
            private int n, m;

            @Override
            public void initialize() {
                n = m = 0;
            }

            @Override
            public Pixel provide() {
                if (m >= height) {
                    m = 0;
                    n++;
                }
                if (n >= width) return done();
                Pixel p = new Pixel(n, m++);
                return p;
            }
        };
    }    
    
    public Iterable<Kernel> kernels() {
        return new Providable<Kernel>() {
            private int x, y;

            @Override
            public void initialize() {
                x = y = 0;
            }

            @Override
            public Kernel provide() {
                if (x >= width) {
                    x = 0;
                    y++;
                }
                if (y >= height) return done();
                Kernel k = new Kernel(x++, y);
                return k;
            }
        };
    }

    public class Kernel extends Pixel {
        
        public double topLeft, top, topRight, left, here, right, 
                bottomLeft, bottom, bottomRight;
        
        public Kernel(int x, int y) {
            super(x, y);
            int yTop = (y == 0 ? 0 : y - 1);
            int xLeft = (x == 0 ? 0 : x - 1);
            int yBottom = (y == (height - 1) ? (height - 1) : y + 1);
            int xRight = (x == (width - 1) ? (width - 1) : x + 1);

            topLeft = getDEM()[xLeft][yTop];
            top = getDEM()[x][yTop];
            topRight = getDEM()[xRight][yTop];
            left = getDEM()[xLeft][y];
            here = getDEM()[x][y];
            right = getDEM()[xRight][y];
            bottomLeft = getDEM()[xLeft][yBottom];
            bottom = getDEM()[x][yBottom];
            bottomRight = getDEM()[xRight][yBottom];
        }
        
        public void setHillshade(double hillshade) {
            getHillshade()[x][y] = hillshade;
        }

    }

    private double[][] getHillshade() {
        return hillshade == null ? hillshade = new double[width][height] : hillshade;
    }
    
    
    public class Pixel {

        int x;
        int y;

        public Pixel(int x, int y) {
            assert x < width && y < height : x + "," + y;
            this.x = x;
            this.y = y;
        }

        public double xNormed() {
            return (double) x / (width - 1);
        }

        public double yNormed() {
            return (double) y / (height - 1);
        }

        public void increaseElevation(double elevation) {
            if (elevation < 0) return;
            getDEM()[x][y] += elevation;
        }

        public double elevation() {
            return getDEM()[x][y];
        }

        public double hillshade() {
            return getHillshade()[x][y];
        }
        
        public boolean hasContourLine() {
            return getContours()[x][y];
        }
        
        public void setContourLine(boolean bool) {
            getContours()[x][y] = bool;
        }

        public void normalizeElevation(double maxElevation) {
            DEM[x][y] = 100.0 * (DEM[x][y] / maxElevation);
        }
        
    }

    public static MapBuilder builder() {
        return new MapBuilder();
    }
    
    public int pixelSize() {
        return width * height;
    }

    public int locationSize() {
        return locations.size();
    }

    public Object hasDEM() {
        return DEM != null;
    }
    
    public MapVisualization getDefauVisualization() {
        // TODO make all algorithms reentrant.
        Map map = this;
        new NormalizeLocationsAlgorithm(map).run();
        new DEMAlgorithm(map).run();
        new NormalizeElevationAlgorithm(map).run();
        new HillshadeAlgorithm(map).run();
        new ContourLineAlgorithm(map).run();
        //return new SketchVisualization(map);
        return new HillshadeVisualization(map);
    }
    
}
