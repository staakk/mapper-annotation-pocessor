package io.github.staakk.map.processor;


import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import io.github.staakk.map.Exclude;

/**
 * Created 13.08.2017.
 */

public class MapperUtils {

    public static boolean methodCanBeMapped(ExecutableElement from, ExecutableElement to) {
        if (!(from.getModifiers().contains(Modifier.PUBLIC)
                && to.getModifiers().contains(Modifier.PUBLIC)
                && from.getAnnotation(Exclude.class) == null)) {
            return false;
        }

        String fromName = from.getSimpleName().toString();
        String toName = to.getSimpleName().toString();

        if (!(fromName.startsWith("get") && toName.startsWith("set"))) {
            return false;
        }

        String fromSuffix = fromName.substring(3);
        String toSuffix = toName.substring(3);

        if (!fromSuffix.equals(toSuffix)) {
            return false;
        }

        List<? extends VariableElement> toParameters = to.getParameters();

        return toParameters.size() == 1
                && toParameters.get(0).asType().equals(from.getReturnType())
                && from.asType().equals(toParameters.get(0).asType());
    }

    public static boolean fieldCanBeMapped(VariableElement from, VariableElement to) {
        return from.getSimpleName().equals(to.getSimpleName())
                && from.getModifiers().contains(Modifier.PUBLIC)
                && to.getModifiers().contains(Modifier.PUBLIC)
                && from.asType().equals(to.asType())
                && from.getAnnotation(Exclude.class) == null;
    }

}
