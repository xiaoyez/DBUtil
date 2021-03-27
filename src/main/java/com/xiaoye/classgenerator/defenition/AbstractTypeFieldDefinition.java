package com.xiaoye.classgenerator.defenition;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 2:55
 */
public abstract class AbstractTypeFieldDefinition extends FieldDefinition {
    protected static Class mustFieldClass = null;

    public AbstractTypeFieldDefinition() {
        fieldClass = mustFieldClass;
    }

    private AbstractTypeFieldDefinition(String modifier, Class fieldClass, String name, String comment) {
        super(modifier, fieldClass, name, comment);
    }

    public AbstractTypeFieldDefinition(String modifier, String name, String comment) {
        super(modifier, mustFieldClass, name, comment);
    }

    private AbstractTypeFieldDefinition(Class fieldClass, String name, String comment) {
        super(fieldClass, name, comment);
    }

    public AbstractTypeFieldDefinition(String name, String comment) {
        super(mustFieldClass, name, comment);
    }

    private AbstractTypeFieldDefinition(Class fieldClass, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(fieldClass, name, comment, annotationDefinitions);
    }

    public AbstractTypeFieldDefinition(String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(mustFieldClass, name, comment, annotationDefinitions);
    }

    private AbstractTypeFieldDefinition(String modifier, Class fieldClass, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, fieldClass, name, comment, annotationDefinitions);
    }

    public AbstractTypeFieldDefinition(String modifier, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, mustFieldClass, name, comment, annotationDefinitions);
    }
}
