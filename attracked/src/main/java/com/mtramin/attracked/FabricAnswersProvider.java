package com.mtramin.attracked;

import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Provider for Fabric Answer Analytics tracking
 */
public class FabricAnswersProvider implements AnalyticsProvider {

    public FabricAnswersProvider(Context context) {
        Fabric.with(context, new Answers());
    }

    @Override
    public void trackEvent(String name, Map<String, Object> data) {
        CustomEvent event = new CustomEvent(name);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof Number) {
                event.putCustomAttribute(entry.getKey(), (Number) value);
            } else if (value instanceof String) {
                event.putCustomAttribute(entry.getKey(), (String) value);
            } else {
                throw new IllegalArgumentException("Invalid data given. Answers only supports String and Number values.");
            }
        }

        Answers.getInstance().logCustom(event);
    }
}
