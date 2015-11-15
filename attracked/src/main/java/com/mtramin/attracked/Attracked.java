package com.mtramin.attracked;

import java.util.List;
import java.util.Map;

/**
 * Created by marvi on 11/15/2015.
 */
public class Attracked {

    private static Attracked instance = null;
    private List<AnalyticsProvider> providers;

    public static Attracked initialize() {
        if (instance != null) {
            throw new IllegalStateException("Attracked is already initialized!");
        }

        instance = new Attracked();
        return instance;
    }

    public static Attracked getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Attracked is not yet initialized. You must call Attracked.initialize() first.");
        }

        return instance;
    }

    public void trackEvent(String name, Map<String, Object> data) {
        for (AnalyticsProvider provider : providers) {
            provider.trackEvent(name, data);
        }
    }
}
