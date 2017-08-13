package io.github.staakk.map.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * Created 12.08.2017.
 */

public class MapperGenerator {

    private Messager messager;
    private Filer filer;

    public MapperGenerator(Messager messager, Filer filer) {
        this.messager = messager;
        this.filer = filer;
    }

    private static MethodSpec getMapperMethod(String methodName, MapperModel model, TypeElement from, TypeElement to) {
        ClassName fromName = ClassName.get(from);
        ClassName toName = ClassName.get(to);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(toName)
                .addParameter(fromName, "from")
                .addStatement("$T to = new $T()", toName, toName);

        for (String field : model.getMappedFields()) {
            builder.addStatement("to." + field + " = from." + field);
        }

        for (String method : model.getMappedMethods()) {
            builder.addStatement("to.set" + method + "(from.get" + method + "())");
        }

        return builder.addStatement("return to")
                .build();
    }

    private static String createMapperClassName(MapperModel model) {
        String fromName = ClassNameUtils.getClassName(
                model.getFrom().getSimpleName().toString()
        );
        String toName = ClassNameUtils.getClassName(
                model.getTo().getSimpleName().toString()
        );
        return fromName + "To" + toName + "Mapper";
    }

    public void generate(MapperModel model) {
        MethodSpec privateConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec mapForward = getMapperMethod(
                "map", model, model.getFrom(), model.getTo());

        TypeSpec mapperClass = TypeSpec.classBuilder(createMapperClassName(model))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(privateConstructor)
                .addMethod(mapForward)
                .build();

        JavaFile javaFile = JavaFile.builder("io.github.staakk.mapper", mapperClass)
                .build();

        try {
            JavaFileObject o = filer.createSourceFile("io.github.staakk.mapper." + mapperClass.name, model.getFrom());
            Writer w = o.openWriter();
            javaFile.writeTo(w);
            w.flush();
            w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
