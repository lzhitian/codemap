package ch.deif.meander;

import ch.deif.meander.Map.Pixel;

/** Computes the nearest neighbor for each pixel
 *<P>
 * Very very naive implementation, needs optimization!
 * 
 * @author Adrian Kuhn
 *
 */
public class NearestNeighborAlgorithm extends MapAlgorithm {

	private Location[][] NN;

	public NearestNeighborAlgorithm(Map map) {
		super(map);
	}

	public NearestNeighborAlgorithm() {
	}

	@Override
	public void run() {
		setup();
		compute();
		update(getMap());
	}

	private void update(Map map) {
		map.setNearestNeighbors(this.NN);
	}

	private void compute() {
		for (Pixel p: getMap().pixels()) {
			int nearestDist2 = Integer.MAX_VALUE;
			Location nearestLocation = null;
			for (Location each: getMap().locations()) {
				double dist2 = Math.pow(p.px - each.px(), 2) + Math.pow(p.py - each.py(), 2);
				if (dist2 < nearestDist2) {
					nearestDist2 = (int) dist2;
					nearestLocation = each;
				}
			}
			NN[p.px][p.py] = nearestLocation;
		}
	}

	private void setup() {
		this.NN = new Location[getMap().getWidth()][getMap().getWidth()];
	}

}
