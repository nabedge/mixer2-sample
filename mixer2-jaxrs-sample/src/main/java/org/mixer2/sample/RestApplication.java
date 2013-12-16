package org.mixer2.sample;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<Class<?>>();
        
        // add "/index" and  "/static" resource.
        set.add(IndexResource.class);
        set.add(StaticResource.class);
        
        return set;
    }

    
    @Override
    public Set<Object> getSingletons() {
        HashSet<Object> set = new HashSet<Object>();
        
        Mixer2Writer mixer2Writer = new Mixer2Writer();
        set.add(mixer2Writer);
        
        return set;
        
    }
}
