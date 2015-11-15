package com.mtramin.attracked;

import java.util.Map;

/**
 * Created by marvi on 11/15/2015.
 */
public interface AnalyticsProvider {

    void trackEvent(String name, Map<String, Object> data);
}
