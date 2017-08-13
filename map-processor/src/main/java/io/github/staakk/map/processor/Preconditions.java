package io.github.staakk.map.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

/**
 * Created 12.08.2017.
 */

public class Preconditions {

    private Messager messager;

    public Preconditions(Messager messager) {
        this.messager = messager;
    }

    /**
     * Checks if {@code element} has default constructor. If default constructor is not present
     * terminates further processing.
     * @param element element to be checked
     */
    public void hasDefaultConstructor(TypeElement element) {
        for (ExecutableElement e :
                ElementFilter.constructorsIn(element.getEnclosedElements())) {
            if (e.getParameters().isEmpty()) {
                return;
            }
        }

        messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Mapper: Missing default constructor in class " + element.getQualifiedName(),
                element
        );
    }

    /**
     * Checks if {@code element} is static
     * @param element element to be checked
     */
    public void isStaticClass(TypeElement element) {
        if (element.getModifiers().contains(Modifier.STATIC)) {
            return;
        }

        messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Mapper: Class " + element.getQualifiedName() + " must be declared static",
                element
        );
    }

}
