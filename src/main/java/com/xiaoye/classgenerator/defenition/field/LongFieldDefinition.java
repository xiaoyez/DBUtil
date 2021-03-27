package com.xiaoye.classgenerator.defenition.field;

import com.xiaoye.classgenerator.defenition.AbstractTypeFieldDefinition;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 2:21
 */
public class LongFieldDefinition extends AbstractTypeFieldDefinition {

    static {
        mustFieldClass = Long.class;
    }

    public LongFieldDefinition() {
    }

    public LongFieldDefinition(String modifier, String name, String comment) {
        super(modifier, name, comment);
    }

    public LongFieldDefinition(String name, String comment) {
        super(name, comment);
    }

    public LongFieldDefinition(String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(name, comment, annotationDefinitions);
    }

    public LongFieldDefinition(String modifier, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, name, comment, annotationDefinitions);
    }
}
