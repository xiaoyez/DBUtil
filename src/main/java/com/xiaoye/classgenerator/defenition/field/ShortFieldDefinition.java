package com.xiaoye.classgenerator.defenition.field;

import com.xiaoye.classgenerator.defenition.AbstractTypeFieldDefinition;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 2:21
 */
public class ShortFieldDefinition extends AbstractTypeFieldDefinition {

    static {
        mustFieldClass = Short.class;
    }

    public ShortFieldDefinition() {
    }

    public ShortFieldDefinition(String modifier, String name, String comment) {
        super(modifier, name, comment);
    }

    public ShortFieldDefinition(String name, String comment) {
        super(name, comment);
    }

    public ShortFieldDefinition(String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(name, comment, annotationDefinitions);
    }

    public ShortFieldDefinition(String modifier, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, name, comment, annotationDefinitions);
    }
}
