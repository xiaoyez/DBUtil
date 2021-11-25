package com.xiaoye.util;

import com.xiaoye.classgenerator.config.ClassDefinitionConfiguration;
import com.xiaoye.classgenerator.constant.Modifer;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;
import com.xiaoye.classgenerator.defenition.field.FieldDefinition;
import com.xiaoye.classgenerator.defenition.type.ClassDefinition;
import com.xiaoye.classgenerator.util.ArrayList;
import com.xy.entity.Table;
import com.xy.util.db.JdbcUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 3:18
 */
public class ClassDefinitionUtil {

    public static ClassDefinition fromTable(Table table){
        Collection<com.xy.entity.Column> columns = table.columns();
        List<FieldDefinition> fieldDefinitions = new ArrayList();
        for (com.xy.entity.Column column : columns) {
            fieldDefinitions.add(new FieldDefinition(JdbcUtils.toJavaType(column.getSqlType()),StringUtil.firstLetterToLowerCase(StringUtil.castUnderlineToCamel(column.getName())),column.getComment()));
        }
        String className = table.getName();
        className = StringUtil.castUnderlineToCamel(className);
        ClassDefinition classDefinition = new ClassDefinition(null,null,null , className,fieldDefinitions,new ClassDefinitionConfiguration());
        classDefinition.addSuperInterface(Serializable.class.getName());
        return classDefinition;
    }

    public static ClassDefinition fromTableSuiteTkMapper(Table table)
    {
        Collection<com.xy.entity.Column> columns = table.columns();
        List<FieldDefinition> fieldDefinitions = new ArrayList();
        for (com.xy.entity.Column column : columns) {
            String fieldName = StringUtil.firstLetterToLowerCase(StringUtil.castUnderlineToCamel(column.getName()));
            FieldDefinition fieldDefinition = FieldDefinition.builder()
                    .modifier(Modifer.PRIVATE)
                    .fieldClass(JdbcUtils.toJavaType(column.getSqlType()))
                    .name(fieldName)
                    .comment(column.getComment())
                    .isStatic(false)
                    .build();
            fieldDefinitions.add(fieldDefinition);
            fieldDefinition.setComment(column.getComment());
            if (column.isPK())
            {
                AnnotationDefinition id = new AnnotationDefinition(Id.class);
                fieldDefinition.addAnnotationDefinition(id);
            }
            else
            {
                AnnotationDefinition columnAnnotationDefinition = new AnnotationDefinition(Column.class);
                fieldDefinition.addAnnotationDefinition(columnAnnotationDefinition);
            }
            if (column.isAutoincrement())
            {
                AnnotationDefinition annotationDefinition = new AnnotationDefinition(GeneratedValue.class);
                annotationDefinition.addParam("strategy", "javax.persistence.GenerationType.IDENTITY");
            }
        }
        String className = table.getName();
        className = StringUtil.castUnderlineToCamel(className);


        ClassDefinitionConfiguration configuration = new ClassDefinitionConfiguration();
        configuration.setNoArgsConstructor(false);
        configuration.setAllArgsConstructor(false);
        configuration.setGetter(false);
        configuration.setSetter(false);
        configuration.setToString(false);

        ClassDefinition classDefinition = ClassDefinition.builder()
                .name(className)
                .configuration(configuration)
                .fieldDefinitions(fieldDefinitions)
                .build();
        classDefinition.addAnnotationDefinition(new AnnotationDefinition(Data.class,null ));
        classDefinition.addAnnotationDefinition(new AnnotationDefinition(NoArgsConstructor.class,null ));
        classDefinition.addAnnotationDefinition(new AnnotationDefinition(AllArgsConstructor.class,null ));
        classDefinition.addAnnotationDefinition(new AnnotationDefinition(Builder.class,null ));
        classDefinition.addSuperInterface(Serializable.class.getName());
        return classDefinition;
    }
}
