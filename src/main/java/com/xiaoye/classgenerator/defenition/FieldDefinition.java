package com.xiaoye.classgenerator.defenition;

import com.xiaoye.classgenerator.constant.Modifer;
import com.xiaoye.classgenerator.constant.Tab;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-13 18:27
 */
@Setter
@Getter
@NoArgsConstructor
public class FieldDefinition extends AbstarctAnnotatedDefinition{

    protected String modifier = Modifer.PRIVATE;

    protected Class fieldClass;

    protected String name;
    //注释
    protected String comment;


    public FieldDefinition(String modifier,Class fieldClass, String name, String comment) {
        this.modifier = modifier;
        this.fieldClass = fieldClass;
        this.name = name;
        this.comment = comment;
    }

    public FieldDefinition(Class fieldClass, String name, String comment) {
        this.fieldClass = fieldClass;
        this.name = name;
        this.comment = comment;
    }

    public FieldDefinition(Class fieldClass, String name, String comment,List<AnnotationDefinition> annotationDefinitions) {
        super(annotationDefinitions);
        this.fieldClass = fieldClass;
        this.name = name;
        this.comment = comment;
    }

    public FieldDefinition(String modifier, Class fieldClass, String name, String comment, List<AnnotationDefinition> annotationDefinitions) {
        super(annotationDefinitions);
        this.modifier = modifier;
        this.fieldClass = fieldClass;
        this.name = name;
        this.comment = comment;
    }

    public String open() {
        if (annotationDefinitions == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
            builder.append(Tab.TAB)
                    .append(annotationDefinitions.toString());
        }
        return builder.toString();
    }

    public String close() {
        return ";\n";
    }

    public String generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(Tab.TAB)
                .append(modifier)
                .append(" ")
                .append(fieldClass.getName().startsWith("java.lang")?fieldClass.getName().substring("java.lang.".length()):fieldClass.getName())
                .append(" ")
                .append(name);
        return builder.toString();
    }
}
