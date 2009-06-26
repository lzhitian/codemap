package ch.deif.meander;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List of locations on a map.
 * 
 * @author Adrian Kuhn
 */
public class LocationList implements Iterable<Location> {

	private List<Location> locations;

	public LocationList() {
		this.locations = new ArrayList<Location>();
	}

	@Override
	public Iterator<Location> iterator() {
		return locations.iterator();
	}

	public int count() {
		return locations.size();
	}

	public Location makeLocation(double x, double y, double elevation) {
		Location location = new Location(x, y, elevation);
		locations.add(location);
		return location;
	}

	public Location at(int index) {
		return locations.get(index);
	}

	protected void normalizePixelXY(int pixelSize) {
		for (Location each: this) {
			each.normalizePixelXY(pixelSize);
		}
	}

	protected void normalizeLocations() {
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		for (Location each: this) {
			minY = Math.min(minY, each.y());
			maxY = Math.max(maxY, each.y());
			minX = Math.min(minX, each.x());
			maxX = Math.max(maxX, each.x());
		}
		double width = maxX - minX;
		double height = maxY - minY;
		for (Location each: this) {
			each.normalizeXY(minX, width, minY, height);
		}
	}

	public Location last() {
		return locations.get(locations.size() - 1);
	}

	public void normalizeElevation() {
		double maxElevation = 0;
		for (Location each: this) {
			maxElevation = Math.max(maxElevation, each.elevation());
		}
		for (Location each: this) {
			each.normalizeElevation(maxElevation);
		}
	}
	
}
