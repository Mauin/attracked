package com.mtramin.attracked;

/**
 * TODO: JAVADOC
 */
public class BindingClass {

    private final String targetClass;
    private final String packageName;
    private final String className;

    public BindingClass(String className, String targetClass, String packageName) {
        this.className = className;
        this.targetClass = targetClass;
        this.packageName = packageName;
    }
}
