package org.codemap.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.codemap.Configuration;
import org.codemap.Point;
import org.codemap.internal.LayoutAlgorithm;
import org.codemap.util.MapScheme;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;

/** Not thread-safe.
 * 
 * @author akuhn
 *
 */
public class MapBuilder {
    
    public static final MapScheme<Double> FILE_LENGTH_SQRT = new MapScheme<Double>() {
        @Override
        public Double forLocation(Point location) {
            return Math.sqrt(new File(location.getDocument()).length());
        }
    };    

    private LatentSemanticIndex lsi = null;

    public MapBuilder addCorpus(Hapax hapax) {
        if (this.lsi != null) throw new IllegalStateException();
        this.lsi = hapax.getIndex();
        return this;
    }

    public Configuration makeMap(Configuration initialConfiguration) {
        if (this.lsi == null) throw new IllegalStateException();
        double[] x = new double[lsi.documentCount()];
        double[] y = new double[lsi.documentCount()];
        Map<String, Point> cachedPoints = initialConfiguration.asMap();
        int n = 0;
        
        boolean doLayout = false;
        for (String document: lsi.documents()) {
            Point p = cachedPoints.get(document);
            if (p == null) {
                p = Point.newRandom(document);
                doLayout = true;
            }
            x[n] = p.x;
            y[n] = p.y;
            n++;
        }
        
        Collection<Point> locations = doLayout ? runLayout(lsi, x, y) : loadCached(lsi, x, y);
        return new Configuration(locations).normalize();
    }

    private Collection<Point> runLayout(LatentSemanticIndex lsi, double[] x, double[] y) {
        LayoutAlgorithm mds = LayoutAlgorithm.fromCorrelationMatrix(lsi, x, y);
        mds.normalize();
        Collection<Point> locations = new ArrayList<Point>();
        int index = 0;
        for (String each: lsi.documents()) {
            locations.add(new Point(mds.x[index], mds.y[index], each));
            index++;
        }
        return locations;
    }

    private Collection<Point> loadCached(LatentSemanticIndex lsi2, double[] x, double[] y) {
        Collection<Point> locations = new ArrayList<Point>();
        int index = 0;
        for (String each: lsi.documents()) {
            locations.add(new Point(x[index], y[index], each));
            index++;
        }
        return locations;        
    }

    public MapBuilder addCorpus(LatentSemanticIndex index) {
        if (this.lsi != null) throw new IllegalStateException();
        this.lsi = index;
        return this;
    }
}