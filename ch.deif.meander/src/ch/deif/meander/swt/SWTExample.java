package ch.deif.meander.swt;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.builder.Meander;

public class SWTExample {

	public static void main(String[] args) {
		MapInstance map = Meander.builder()
			.addCorpus(Hapax.legomenon().makeCorpus("../ch.akuhn.hapax", ".java"))
			.makeMap()
			.withSize(512);
		CodemapVisualization visual = new CodemapVisualization(map);
		Background layer = new Background();
		visual.add(layer);
		layer.children.add(new WaterBackground());
		layer.children.add(new ShoreLayer());
		layer.children.add(new HillshadeLayer());
		visual.add(new LabelOverlay());
		visual.add(new CurrSelectionOverlay().setSelection(new MapSelection()));
		visual.openAndBlock();
	}

}