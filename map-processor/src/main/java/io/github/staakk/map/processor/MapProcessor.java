package io.github.staakk.map.processor;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import io.github.staakk.map.Map;


@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.staakk.map.Map")
public class MapProcessor extends AbstractProcessor {

    private Preconditions preconditions;
    private MapperGenerator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        preconditions = new Preconditions(processingEnv.getMessager());
        generator = new MapperGenerator(processingEnv.getMessager(), processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Collection<? extends Element> annotatedElements =
                roundEnv.getElementsAnnotatedWith(Map.class);

        ArrayList<TypeElement> elements = new ArrayList<>();
        for (Element e : annotatedElements) {
            if (e instanceof TypeElement) {
                elements.add(((TypeElement) e));
            }
        }

        List<TypeElement> types = Collections.unmodifiableList(elements);
        for (TypeElement e : types) {
            preconditions.hasDefaultConstructor(e);
            preconditions.isStaticClass(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "processing elements");

            Element mapElement = processingEnv.getElementUtils().getTypeElement(Map.class.getName());
            TypeMirror mapType = mapElement.asType();

            AnnotationValue value = null;
            for (AnnotationMirror am : e.getAnnotationMirrors()) {
                if (am.getAnnotationType().equals(mapType)) {
                    for (java.util.Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                        if ("value".equals(entry.getKey().getSimpleName().toString())) {
                            value = entry.getValue();
                        }
                    }
                }
            }

            if (value != null) {
                TypeElement elem = processingEnv.getElementUtils().getTypeElement(value.toString().replace(".class", ""));
                MapperModel model = new MapperModel(e, elem);
                generator.generate(model);
            }
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
