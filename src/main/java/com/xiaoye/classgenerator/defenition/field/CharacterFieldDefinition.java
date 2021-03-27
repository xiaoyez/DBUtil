package com.xiaoye.classgenerator.defenition.field;

import com.xiaoye.classgenerator.defenition.AbstractTypeFieldDefinition;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 2:21
 */
public class CharacterFieldDefinition extends AbstractTypeFieldDefinition {

    static {
        mustFieldClass = Character.class;
    }

    public CharacterFieldDefinition() {
    }

    public CharacterFieldDefinition(String modifier, String name, String comment) {
        super(modifier, name, comment);
    }

    public CharacterFieldDefinition(String name, String comment) {
        super(name, comment);
    }

    public CharacterFieldDefinition(String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(name, comment, annotationDefinitions);
    }

    public CharacterFieldDefinition(String modifier, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(modifier, name, comment, annotationDefinitions);
    }
}
