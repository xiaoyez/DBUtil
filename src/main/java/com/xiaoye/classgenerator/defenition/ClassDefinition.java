package com.xiaoye.classgenerator.defenition;

import com.xiaoye.classgenerator.config.ClassGeneratorConfiguration;
import com.xiaoye.classgenerator.constant.Modifer;
import com.xiaoye.classgenerator.constant.Tab;
import com.xiaoye.classgenerator.util.ArrayList;
import com.xiaoye.classgenerator.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-13 18:45
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassDefinition extends AbstarctAnnotatedDefinition{

    private String modifier;

    private String packageName;

    private String name;

    private List<FieldDefinition> fieldDefinitions = null;

    private ClassGeneratorConfiguration configuration;

    public ClassDefinition(String modifier, String packageName, String name) {
        this.modifier = modifier;
        this.packageName = packageName;
        this.name = name;
        configuration = new ClassGeneratorConfiguration();
    }

    @Override
    public String open() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ")
                .append(packageName)
                .append(";\n\n");
        if (annotationDefinitions != null) {
            for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
                builder.append(annotationDefinition.toString());
            }
        }

        builder.append(modifier)
                .append(" class ")
                .append(name)
                .append("{\n");
        return builder.toString();
    }

    public void addFieldDefinition(FieldDefinition fieldDefinition)
    {
        if (fieldDefinitions == null)
            fieldDefinitions = new ArrayList<>();
        fieldDefinitions.add(fieldDefinition);
    }

    @Override
    public String close() {
        return "}";
    }

    @Override
    public String generate() {
        StringBuilder builder = new StringBuilder();
        generateFields(builder);

        generateConstructor(configuration,builder);

        generateMethods(configuration,builder);

        generateToString(configuration,builder);
        return builder.toString();
    }

    private void generateMethods(ClassGeneratorConfiguration configuration, StringBuilder builder) {
        if (configuration.isSetter() && fieldDefinitions != null)
        {
            generateSetter(builder);
            generateGetter(builder);
        }
    }

    private void generateGetter(StringBuilder builder) {
        for (FieldDefinition fieldDefinition : fieldDefinitions) {
            builder.append(Tab.TAB)
                    .append("public ")
                    .append(StringUtil.fromClass(fieldDefinition.getFieldClass()))
                    .append(" get")
                    .append(StringUtil.firstLetterToUpper(fieldDefinition.getName()))
                    .append("(){\n")
                    .append(Tab.TAB)
                    .append(Tab.TAB)
                    .append("return ")
                    .append(fieldDefinition.getName())
                    .append(";\n")
                    .append(Tab.TAB)
                    .append("}\n");
        }
    }

    private void generateSetter(StringBuilder builder) {
        for (FieldDefinition fieldDefinition : fieldDefinitions) {
            builder.append(Tab.TAB)
                    .append(Modifer.PUBLIC)
                    .append(" void ")
                    .append("set")
                    .append(StringUtil.firstLetterToUpper(fieldDefinition.getName()))
                    .append("(")
                    .append(StringUtil.fromClass(fieldDefinition.getFieldClass()))
                    .append(" ")
                    .append(fieldDefinition.getName())
                    .append("){\n")
                    .append(Tab.TAB)
                    .append(Tab.TAB)
                    .append("this.")
                    .append(fieldDefinition.getName())
                    .append(" = ")
                    .append(fieldDefinition.getName())
                    .append(";\n")
                    .append(Tab.TAB)
                    .append("}\n");

        }
    }

    private void generateFields(StringBuilder builder) {
        if (fieldDefinitions != null)
        {
            fieldDefinitions.forEach((fieldDefinition -> {
                builder.append(fieldDefinition.toString())
                        .append("\n");
            }));
        }
    }

    private void generateConstructor(ClassGeneratorConfiguration configuration, StringBuilder builder) {
        if (configuration.isNoArgsConstructor())
        {
            builder.append(Tab.TAB + "public "+name + "(){}\n");
        }
        if (configuration.isAllArgsConstructor())
        {
            builder.append(Tab.TAB)
                    .append("public ")
                    .append(name)
                    .append("(");
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                builder.append(StringUtil.fromClass(fieldDefinition.getFieldClass()))
                        .append(" ")
                        .append(fieldDefinition.getName())
                        .append(",");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append("){\n");
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                builder.append(Tab.TAB)
                        .append(Tab.TAB)
                        .append("this.")
                        .append(fieldDefinition.getName())
                        .append(" = ")
                        .append(fieldDefinition.getName())
                        .append(";\n");
            }
            builder.append(Tab.TAB)
                    .append("}\n");
        }
    }

    public void generateToString(ClassGeneratorConfiguration configuration,StringBuilder builder)
    {
        if (!configuration.isToString())
            return;
        builder.append(Tab.TAB)
                .append("public String toString(){\n")
                .append(Tab.TAB)
                .append(Tab.TAB)
                .append("return \"")
                .append(name)
                .append("{\" +\n");
        if (fieldDefinitions != null)
        {
            for (int i = 0; i < fieldDefinitions.size(); i++) {
                FieldDefinition fieldDefinition = fieldDefinitions.get(i);
                builder.append(Tab.TAB)
                        .append(Tab.TAB)
                        .append(Tab.TAB)
                        .append(Tab.TAB)
                        .append((i==0)?"\"":"\", ")
                        .append(fieldDefinition.getName())
                        .append("=")
                        .append(fieldDefinition.getFieldClass().equals(String.class)?"'":"")
                        .append("\"")
                        .append(" + ")
                        .append(fieldDefinition.getName())
                        .append(" + ")
                        .append(fieldDefinition.getFieldClass().equals(String.class)?"'\\'' +":"")
                        .append("\n");
            }
        }
        builder.append(Tab.TAB)
                .append(Tab.TAB)
                .append(Tab.TAB)
                .append(Tab.TAB)
                .append("'}';\n")
                .append(Tab.TAB)
                .append("}\n");
    }


}
