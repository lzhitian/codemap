package org.codemap.resources;

import java.util.Collection;

import ch.akuhn.values.Value;
import ch.deif.meander.map.MapValues;


public class EclipseMapValues extends MapValues {

    public final Value<Collection<String>> projects;
    public final Value<Collection<String>> extensions;
    
    public EclipseMapValues(EclipseMapValuesBuilder make) {
        super(make);
        
        projects = make.projectsValue();
        extensions = make.extensionsValue();
        
    }

}