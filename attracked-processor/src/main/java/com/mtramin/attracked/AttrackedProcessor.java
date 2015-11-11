package com.mtramin.attracked;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class AttrackedProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.elementUtils = processingEnv.getElementUtils();
        this.messager = processingEnv.getMessager();
        this.typeUtils = processingEnv.getTypeUtils();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();

        types.add(Event.class.getCanonicalName());
        types.add(Attribute.class.getCanonicalName());

        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<TypeElement, BindingClass> classes = new HashMap<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(Event.class)) {
            try {
                System.out.printf("Element " + element.toString());
                if (element.getKind() != ElementKind.CLASS) {
                    error(element, "Event annotations can only be applied to classes!");
                    return false;
                }

                TypeElement typeElement = (TypeElement) element;
                BindingClass bindingClass = getOrCreateBinding(classes, typeElement);

                System.out.printf("TEST");
                // TODO generate stuff!
            } catch (Exception e) {
                error(element, "Unable to generate Analytics Event.\n\n%s", e.getMessage());
            }
        }

        return true;
    }

    private BindingClass getOrCreateBinding(Map<TypeElement, BindingClass> classes, TypeElement typeElement) {
        BindingClass bindingClass = classes.get(typeElement);
        if (bindingClass != null) {
            return bindingClass;
        }

        String targetClass = typeElement.getQualifiedName().toString();
        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        String className = targetClass.substring(packageName.length() + 1).replace('.', '$') + "$$Attracked";

        bindingClass = new BindingClass(className, targetClass, packageName);
        classes.put(typeElement, bindingClass);
        return null;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
