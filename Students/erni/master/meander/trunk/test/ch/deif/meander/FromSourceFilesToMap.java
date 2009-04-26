package ch.deif.meander;

import java.io.File;
import java.util.Iterator;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.viz.LabelsOverlay;
import ch.deif.meander.viz.Layers;

public class FromSourceFilesToMap {

    public static void main(String... args) {
        
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        Importer importer = new Importer(tdm);
        importer.importAllFiles(new File("../JExample"), ".java");
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex lsi = tdm.createIndex();
        MDS mds = MDS.fromCorrelationMatrix(lsi);
        MapBuilder builder = Map.builder().size(512, 512);
        Iterator<Document> iterator = lsi.documents.iterator();
        for (int n = 0; n < mds.x.length; n++) {
            Document each = iterator.next();
            builder.location(mds.x[n], mds.y[n], Math.sqrt(each.termSize()), each);
        }
        builder.normalizeXY();
        Map map = builder.done();
        new DEMAlgorithm(map).run();
        new NormalizeElevationAlgorithm(map).run();
        new HillshadeAlgorithm(map).run();
        new ContourLineAlgorithm(map).run();
        Layers layers = new Layers(map);
        layers.useHillshading();
        layers.add(new LabelsOverlay(map));
        layers.openApplet();
    }
    
}
