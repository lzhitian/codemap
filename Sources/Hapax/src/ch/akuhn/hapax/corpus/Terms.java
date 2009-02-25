package ch.akuhn.hapax.corpus;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import ch.akuhn.util.Bag;
import ch.akuhn.util.PrintOn;

public class Terms extends Bag<String> implements ScannerClient {

    public Terms() {
        // do nothing
    }

    public Terms(File file) {
        new CamelCaseScanner().client(this).onFile(file).run();
    }

    public Terms(String text) {
        new CamelCaseScanner().client(this).onString(text).run();
    }
    
    public Terms(Collection<String> strings) {
        this.addAll(strings); // #addAll handles "instance of bag" special case
    }

    public Terms(InputStream stream) {
        new CamelCaseScanner().client(this).onStream(stream).run();
    }

    public Terms stem() {
        Stemmer stemmer = new PorterStemmer();
        Terms terms = new Terms();
        for (Count<String> each: this.counts()) {
            terms.add(stemmer.stem(each.element), each.count);
        }
        return terms;
    }

    public Terms toLowerCase() {
        Terms terms = new Terms();
        for (Count<String> each: this.counts()) {
            terms.add(each.element.toString().toLowerCase(), each.count);
        }
        return terms;
    }

    //@Override
    public void yield(CharSequence term) {
        this.add(term.toString());
    }
    
    public Terms intern() {
        Terms terms = new Terms();
        for (Count<String> each: this.counts()) {
            terms.add(each.element.intern(), each.count);
        }
        return terms;
    }

    public void storeOn(Appendable app) {
        PrintOn out = new PrintOn(app);
        int count = -1;
        for (Count<String> each: sortedCounts()) {
            if (each.count != count) out.print(count = each.count).space();
            out.append(each.element).space();
        }
        out.cr();
    }
    
}
