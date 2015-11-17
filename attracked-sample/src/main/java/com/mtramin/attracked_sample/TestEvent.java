package com.mtramin.attracked_sample;

import com.mtramin.attracked.Attribute;
import com.mtramin.attracked.Event;

/**
 * Test event to track something with crashlytics
 */
@Event(name = "test")
public class TestEvent {
    @Attribute(name = "hypelevel")
    double hypelevel;

    @Attribute(name = "wouldwedoitagain")
    String yes;
}
