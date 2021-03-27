package com.xiaoye.classgenerator.defenition.field;

import com.xiaoye.classgenerator.defenition.AbstractTypeFieldDefinition;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 2:21
 */
public class StringFieldDefinition extends AbstractTypeFieldDefinition {

    static {
        mustFieldClass = String.class;
    }

    public StringFieldDefinition() {
    }

    public StringFieldDefinition(String modifier, String name, String comment) {
        super(modifier, name, comment);
    }

    public StringFieldDefinition(String name, String comment) {
        super(name, comment);
    }

    public StringFieldDefinition(String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(name, comment, annotationDefinitions);
    }

    public StringFieldDefinition(String modifier, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, name, comment, annotationDefinitions);
    }
}
