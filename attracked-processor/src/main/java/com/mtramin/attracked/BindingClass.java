package com.mtramin.attracked;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * TODO: JAVADOC
 */
public class BindingClass {

    private final String targetClass;
    private final String packageName;
    private final String className;
    private final Map<String, EventBinding> eventBindings;

    public BindingClass(String className, String targetClass, String packageName) {
        this.className = className;
        this.targetClass = targetClass;
        this.packageName = packageName;
        eventBindings = new HashMap<>();
    }

    public void parseBindingData(TypeElement typeElement) {
        EventBinding eventBinding = new EventBinding(typeElement);
        if (eventBindings.containsKey(eventBinding.name)) {
            throw new IllegalStateException("Duplicate analytics event name!");
        }

        eventBindings.put(eventBinding.name, eventBinding);
    }

    public void writeToFiler(Filer filer) {
        
    }

    private class EventBinding {
        private String name;
        private Map<String, TypeMirror> attributes;

        public EventBinding(TypeElement element) {
            Event annotation = element.getAnnotation(Event.class);

            this.name = annotation.name();
            attributes = new HashMap<>();

            parseAnnotatedFields(element);
            System.out.println("have attributes " + attributes.size());
        }

        private void parseAnnotatedFields(TypeElement element) {
            for (Element enclosed : element.getEnclosedElements()) {
                parseField(enclosed);
            }
        }

        private void parseField(Element enclosed) {
            if (enclosed.getKind() != ElementKind.FIELD) {
                return;
            }

            Attribute attributeAnnotation = enclosed.getAnnotation(Attribute.class);
            if (attributeAnnotation == null) {
                return;
            }
            this.attributes.put(attributeAnnotation.name(), enclosed.asType());
        }
    }
}
