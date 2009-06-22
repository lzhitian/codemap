package hapax.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class SmallDocumentsTest {

    @Test
    public void corpusWithSmallDocuments() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.makeDocument("m1").addTerms(new Terms("Lorem ipsum dolor."));
        tdm.makeDocument("m2").addTerms(new Terms("Lorem ipsum dolor."));
        tdm.makeDocument("m3").addTerms(new Terms("Lorem ipsum dolor."));
        LatentSemanticIndex lsi = tdm.rejectAndWeight().createIndex();
        assertEquals(3, lsi.documentCount());
        assertEquals(3, lsi.rankDocumentsByQuery("Lorem").size());
    }
    
    @Test
    public void corpusWithoutDocuments() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        LatentSemanticIndex lsi = tdm.rejectAndWeight().createIndex();
        assertEquals(0, lsi.documentCount());
        assertEquals(0, lsi.rankDocumentsByQuery("Lorem").size());
    }
    
    @Test
    public void corpusWithoutOneDocuments() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.makeDocument("m1").addTerms(new Terms("Lorem ipsum dolor."));
        LatentSemanticIndex lsi = tdm.rejectAndWeight().createIndex();
        assertEquals(1, lsi.documentCount());
        assertEquals(1, lsi.rankDocumentsByQuery("Lorem").size());
    }

}
