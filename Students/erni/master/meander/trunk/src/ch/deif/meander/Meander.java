package ch.deif.meander;

import java.io.File;
import java.util.Iterator;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.corpus.CorpusBuilder;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.MapVisualization;

/**
 * Creates maps from source files
 * 
 * <PRE>
 * Meander.script().addDocuments(&quot;../JExample&quot;, &quot;.java&quot;).makeMap().useHillshading().add(LabelsOverlay.class).openApplet();
 *</PRE>
 * 
 * @author Adrian Kuhn
 */
public class Meander {

	public Meander(Hapax hapax) {
		this.hapax = hapax;
	}

	public Meander() {
		// do nothing
	}

	public static Meander script() {
		return new Meander();
	}
	
	public static Meander with(Hapax hapax) {
		return new Meander(hapax);
	}

	private TermDocumentMatrix tdm;
	private Map map;
	private Layers layers;
	private boolean doneDEM = false;
	private MDS mds;
	private Hapax hapax;
	private LatentSemanticIndex lsi;

	public Meander addDocuments(String folder, String... extensions) {
		if (tdm == null) tdm = new TermDocumentMatrix();
		assert map == null : "Cannot call #addDocuments after #makeMap.";
		CorpusBuilder importer = new CorpusBuilder(tdm);
		importer.importAllFiles(new File(folder), extensions);
		return this;
	}

	public Meander useCorpus(TermDocumentMatrix tdm) {
		assert this.tdm == null : "Cannot call #useCorpus after #addDocuments.";
		assert map == null : "Cannot call #useCorpus after #makeMap.";
		this.tdm = tdm;
		return this;
	}

	public Meander makeMap() {
		return makeMap(null);
	}

	public Meander doTheNumberCrunching() {
		assert (tdm != null || hapax != null) : "Must call #addDocuments first (or create with a Hapax).";
		if (mds != null) return this;
		if (hapax == null) {
			tdm = tdm.rejectAndWeight();
			lsi = tdm.createIndex();
		}
		else {
			lsi = hapax.getIndex();
		}
		mds = MDS.fromCorrelationMatrix(lsi);
		mds.normalize();
		return this;
	}
	
	public Meander makeMap(String version) {
		this.doTheNumberCrunching();
		MapBuilder builder = Map.builder().pixelSize(MeanderApplet.PIXELSCALE);
		Iterator<Document> iterator = lsi.documents.iterator();
		for (int n = 0; n < mds.x.length; n++) {
			Document each = iterator.next();
			if (version != null && !each.version().equals(version)) continue;
			builder.location(mds.x[n], mds.y[n], Math.sqrt(each.termSize())).document(each);
		}
		builder.normalizeElevation();
		map = builder.done();
		layers = new Layers(map);
		doneDEM = false;
		return this;
	}
	
	public Meander useHillshading() {
		if (!doneDEM) {
			new DEMAlgorithm(map).run();
			new NormalizeElevationAlgorithm(map).run();
			new HillshadeAlgorithm(map).run();
			new ContourLineAlgorithm(map).run();
			doneDEM = true;
		}
		layers.useHillshading();
		return this;
	}

	public Meander add(Class<? extends MapVisualization> overlay) {
		layers.add(overlay);
		return this;
	}

	public Meander openApplet() {
		layers.openApplet();
		return this;
	}

	public MapVisualization getVisualization() {
		return layers;
	}

}