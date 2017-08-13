package io.github.staakk.map.processor;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import io.github.staakk.map.Exclude;

/**
 * Created 12.08.2017.
 */

public class MapperModel {

    private final TypeElement from;
    private final TypeElement to;

    private final List<String> mappedFields;
    private final List<String> mappedMethods;

    public MapperModel(TypeElement from, TypeElement to) {
        this.from = from;
        this.to = to;
        this.mappedFields = new ArrayList<>();
        this.mappedMethods = new ArrayList<>();

        List<VariableElement> fromFields = ElementFilter.fieldsIn(from.getEnclosedElements());
        List<VariableElement> toFields = ElementFilter.fieldsIn(to.getEnclosedElements());

        for (VariableElement f : fromFields) {
            for (VariableElement t : toFields) {
                if (MapperUtils.fieldCanBeMapped(f, t)) {
                    mappedFields.add(t.getSimpleName().toString());
                }
            }
        }

        List<ExecutableElement> fromMethods = ElementFilter.methodsIn(from.getEnclosedElements());
        List<ExecutableElement> toMethods = ElementFilter.methodsIn(to.getEnclosedElements());

        for (ExecutableElement f : fromMethods) {
            for (ExecutableElement t : toMethods) {
                if (MapperUtils.methodCanBeMapped(f, t)) {
                    mappedMethods.add(f.getSimpleName().toString().substring(3));
                }
            }
        }
    }

    public TypeElement getFrom() {
        return from;
    }

    public TypeElement getTo() {
        return to;
    }

    public List<String> getMappedFields() {
        return mappedFields;
    }

    public List<String> getMappedMethods() {
        return mappedMethods;
    }

}
