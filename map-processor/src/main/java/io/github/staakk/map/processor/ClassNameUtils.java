package io.github.staakk.map.processor;

/**
 * Created 13.08.2017.
 */

public class ClassNameUtils {

    public static String getClassName(String className) {
        String[] parts = className.split("\\.");
        return parts[parts.length - 1];
    }

}
