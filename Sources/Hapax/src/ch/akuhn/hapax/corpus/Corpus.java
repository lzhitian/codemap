package ch.akuhn.hapax.corpus;

import static ch.akuhn.util.Get.each;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.util.CacheMap;
import ch.akuhn.util.Files;
import ch.akuhn.util.Throw;

@FamePackage("Hapax")
@FameDescription("Corpus")
public class Corpus {

    @FameProperty
    private Collection<Document> documents;
    @FameProperty
    private Collection<VersionNumber> versions;
    private Map<String,VersionNumber> versionMap;

    public Corpus() {
        this.documents = new ArrayList<Document>();
        this.versions = new ArrayList<VersionNumber>();
    }
    
    private VersionNumber getVersion(String name) {
        if (versionMap == null) versionMap = new VersionMap();
        return versionMap.get(name);
    }
    
    @SuppressWarnings("serial")
    private class VersionMap extends CacheMap<String,VersionNumber> {
        public VersionMap() { 
            for (VersionNumber each: versions) this.put(each.name(), each); 
        }
        @Override
        public VersionNumber initialize(String name) {
            VersionNumber version = new VersionNumber(name);
            versions.add(version);
            return version;
        }
        
    }

    public Iterable<Document> documents() {
        return documents;
    }

    public int documentSize() {
        return documents.size();
    }

    public void add(Document document) {
        documents.add(document);
    }
    
    public void addAll(Corpus other) {
        for (Document each: other.documents) this.add(each);
    }

    public Corpus importAllFiles(File folder, String... extensions) {
        for (File each: Files.find(folder, extensions)) {
            addDocument(each.getAbsolutePath(), new Terms(each));
        }
        return this;
    }
    
    public Corpus importAllZipArchives(File folder, String... extensions) {
        for (File file: Files.find(folder, ".zip", ".jar")) {
            this.importZipArchive(file, extensions);
        }
        return this;
    }

    public Corpus importZipArchive(File file, String... extensions) {
        try {
            ZipFile zip = new ZipFile(file);
            for (ZipEntry entry: each(zip.entries())) {
                for (String suffix: extensions) {
                    if (!entry.getName().endsWith(suffix)) continue;
                    InputStream in = zip.getInputStream(entry);
                    Terms terms = new Terms(in).intern();
                    this.addDocument(entry.getName(), file.getName(), terms);
                }
            }
            return this;
        } catch (ZipException ex) {
            throw Throw.exception(ex);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public void addDocument(String name, Collection<String> terms) {
        this.add(new Document(name, terms));
    }

    public void addDocument(String name, String version, Collection<String> terms) {
        this.add(new Document(name, getVersion(version), terms));
    }

    public Terms terms() {
        Terms terms = new Terms();
        for (Document each: documents) terms.addAll(each.terms());
        return terms;
    }

    public int termSize() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("Corpus (%d documents, %d terms)", documentSize(), termSize());
    }

    public void importZipArchive(String name, String extensions) {
        this.importZipArchive(new File(name), extensions);
    }

    
    
}