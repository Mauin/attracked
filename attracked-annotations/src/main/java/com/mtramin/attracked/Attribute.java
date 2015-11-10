package com.mtramin.attracked;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * TODO: JAVADOC
 */
@Target(ElementType.FIELD)
public @interface Attribute {
    String name();
}
