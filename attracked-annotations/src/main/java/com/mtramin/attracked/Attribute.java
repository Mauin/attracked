package com.mtramin.attracked;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Tracking event attribute
 */
@Target(ElementType.FIELD)
public @interface Attribute {
    String name();
}
