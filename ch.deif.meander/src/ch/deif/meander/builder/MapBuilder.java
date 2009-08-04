package ch.deif.meander.builder;

import java.io.File;
import java.util.Map;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.Pair;
import ch.deif.meander.Configuration;
import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public interface MapBuilder {

	static final MapScheme<Double> FILE_LENGTH_SQRT = new MapScheme<Double>() {
		@Override
		public Double forLocation(Point location) {
			return Math.sqrt(new File(location.getDocument()).length());
		}
	};

	MapBuilder addCorpus(Hapax hapax);

	Configuration makeMap();

	Configuration makeMap(Map<String, Pair<Double, Double>> map);

	MapBuilder addCorpus(LatentSemanticIndex lsi);

}
