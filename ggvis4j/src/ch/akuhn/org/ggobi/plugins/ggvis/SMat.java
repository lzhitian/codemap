package ch.akuhn.org.ggobi.plugins.ggvis;

import java.util.Arrays;

/** Symmetric matrix without diagonal.
 * <P>
 * This class is not thread-safe. 
 *
 * @author Adrian Kuhn
 *
 */
public class SMat {

    private static final int BIN = 100;
    public final double[][] vals;

    public SMat(int size) {
        vals = new double[size][];
        for (int n = 0; n < vals.length; n++) vals[n] = new double[n];
    }

    public void fill(double value) {
        for (int n = 0; n < vals.length; n++) Arrays.fill(vals[n], value);
    }

    public int[] getHistogram() {
        double max = Double.MIN_VALUE;
        for (double[] row: vals) {
            for (double each: row) {
                max = Math.max(max, each);
            }
        }
        max = 2.5; // FIXME
        int[] bins = new int[BIN];
        for (double[] row: vals) {
            for (double each: row) {
                int index = (int) Math.floor(each / max * (BIN - 1));
                bins[Math.min(BIN - 1, index)]++;
            }
        }
        return bins;
    }

}
