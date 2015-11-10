package com.mtramin.attracked_sample;

import com.mtramin.attracked.Attribute;
import com.mtramin.attracked.Event;

/**
 * TODO: JAVADOC
 */
@Event(name="test")
public class TestEvent {
    @Attribute(name="one")
    String one;

    @Attribute(name="two")
    String two;
}
