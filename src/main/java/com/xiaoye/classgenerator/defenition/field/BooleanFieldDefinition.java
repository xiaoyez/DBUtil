package com.xiaoye.classgenerator.defenition.field;

import com.xiaoye.classgenerator.defenition.AbstractTypeFieldDefinition;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 2:21
 */
public class BooleanFieldDefinition extends AbstractTypeFieldDefinition {

    static {
        mustFieldClass = Boolean.class;
    }

    public BooleanFieldDefinition() {
    }

    public BooleanFieldDefinition(String modifier, String name, String comment) {
        super(modifier, name, comment);
    }

    public BooleanFieldDefinition(String name, String comment) {
        super(name, comment);
    }

    public BooleanFieldDefinition(String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(name, comment, annotationDefinitions);
    }

    public BooleanFieldDefinition(String modifier, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, name, comment, annotationDefinitions);
    }
}
