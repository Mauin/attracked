package com.mtramin.attracked;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Class representing data of an Event class
 */
public class EventClass {

    private final String targetClass;
    private final String packageName;
    private final String className;

    private EventBinding binding;

    public EventClass(String className, String targetClass, String packageName) {
        this.className = className;
        this.targetClass = targetClass;
        this.packageName = packageName;
    }

    public void parseBindingData(TypeElement element) {
        if (binding != null) {
            throw new IllegalStateException("Duplicate analytics event eventName!");
        }
        this.binding = new EventBinding(element);
    }

    public void writeToFiler(Filer filer) throws IOException {
        ClassName targetClassName = ClassName.get(packageName, targetClass);
        TypeSpec.Builder attracked = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(emptyConstructor())
                .addMethod(writeAnalyticsMethod())
                .addMethod(writeTrackObjectMethod());

        System.out.println("Done with generating");

        JavaFile javaFile = JavaFile.builder(packageName, attracked.build()).build();
        javaFile.writeTo(filer);

        System.out.println("Wrote file");
    }

    private MethodSpec writeTrackObjectMethod() {
        return createBaseAnalyticsMethod()
                .addParameter(TypeName.get(binding.type), "eventData")
                .build();
    }

    private MethodSpec writeAnalyticsMethod() {
        MethodSpec.Builder baseAnalyticsMethod = createBaseAnalyticsMethod();
        for (Map.Entry<String, AnnotatedField> entry : binding.annotatedFields.entrySet()) {
            baseAnalyticsMethod.addParameter(TypeName.get(entry.getValue().type), entry.getValue().parameterName);
        }

        return baseAnalyticsMethod.build();
    }

    private MethodSpec.Builder createBaseAnalyticsMethod() {
        String methodName = "track" + binding.name.substring(0, 1).toUpperCase() + binding.name.substring(1);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class);

        methodBuilder.addStatement("$T<String, Object> data = new $T<>()", Map.class, HashMap.class);

        for (Map.Entry<String, AnnotatedField> entry : binding.annotatedFields.entrySet()) {
            methodBuilder.addStatement("data.put($S, $N)", entry.getValue().parameterName, entry.getValue().parameterName);
        }

        ClassName attracked = ClassName.get("com.mtramin.attracked", "Attracked");
        methodBuilder.addStatement("$T.getInstance().trackEvent($S, data)", attracked, binding.name);

        return methodBuilder;
    }


    private MethodSpec emptyConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build();
    }

    private class EventBinding {
        private TypeMirror type;
        private String name;
        private Map<String, AnnotatedField> annotatedFields;

        public EventBinding(TypeElement element) {
            Event annotation = element.getAnnotation(Event.class);

            this.type = element.asType();
            this.name = annotation.name();
            annotatedFields = new HashMap<>();

            parseAnnotatedFields(element);
            System.out.println("have annotatedFields " + annotatedFields.size());
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

            this.annotatedFields.put(enclosed.getSimpleName().toString(), new AnnotatedField(attributeAnnotation.name(), enclosed.asType()));
        }
    }

    private class AnnotatedField {
        private final String parameterName;
        private final TypeMirror type;

        public AnnotatedField(String parameterName, TypeMirror type) {
            this.parameterName = parameterName;
            this.type = type;
        }
    }
}
