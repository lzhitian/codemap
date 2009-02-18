package meander.jsme2009.eclipse;

import java.io.File;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class CorpusExtraction {

    public static void main(String... args) {
        
        // XXX must run with -Xmx128M for greater justice.
        
        Corpus corpus = new Corpus();
        corpus.importAllZipArchivesPackageWise(new File("data/eclipse"), ".java");
        System.out.println(corpus);
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus);
        System.out.println(tdm.density());
        
    }
    
}
