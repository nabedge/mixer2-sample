package org.mixer2.sample.config;

import org.mixer2.Mixer2Engine;

public class Mixer2EngineSingleton {

    private Mixer2EngineSingleton() {}

    private static class SingletonHolder {
        private static final Mixer2Engine instance = new Mixer2Engine();
    }

    public static Mixer2Engine getInstance() {
        return SingletonHolder.instance;
    }
}
