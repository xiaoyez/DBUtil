package com.xiaoye.util;

import com.xiaoye.classgenerator.config.ClassDefinitionConfiguration;
import com.xiaoye.classgenerator.constant.Modifer;
import com.xiaoye.classgenerator.defenition.AnnotationDefinition;
import com.xiaoye.classgenerator.defenition.field.FieldDefinition;
import com.xiaoye.classgenerator.defenition.type.ClassDefinition;
import com.xiaoye.classgenerator.util.ArrayList;
import com.xiaoye.support.Table;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 3:18
 */
public class ClassDefinitionUtil {

    public static ClassDefinition fromTable(Table table){
        List<Table.Column> columns = table.getColumns();
        List<FieldDefinition> fieldDefinitions = new ArrayList();
        for (Table.Column column : columns) {
            fieldDefinitions.add(new FieldDefinition(column.type,StringUtil.firstLetterToLowerCase(StringUtil.castUnderlineToCamel(column.name)),column.comment));
        }
        String className = table.getName();
        className = StringUtil.castUnderlineToCamel(className);
        ClassDefinition classDefinition = new ClassDefinition(null,null,null , className,fieldDefinitions,new ClassDefinitionConfiguration());
        classDefinition.addSuperInterface(Serializable.class.getName());
        return classDefinition;
    }

    public static ClassDefinition fromTableSuiteTkMapper(Table table)
    {
        List<Table.Column> columns = table.getColumns();
        List<FieldDefinition> fieldDefinitions = new ArrayList();
        for (Table.Column column : columns) {
            String fieldName = StringUtil.firstLetterToLowerCase(StringUtil.castUnderlineToCamel(column.name));
            FieldDefinition fieldDefinition = new FieldDefinition(column.type, fieldName, column.comment);
            fieldDefinition.addAnnotationDefinition(new AnnotationDefinition(Column.class,null ));
            fieldDefinitions.add(fieldDefinition);
            fieldDefinition.setComment(column.comment);
        }
        String className = table.getName();
        className = StringUtil.castUnderlineToCamel(className);


        ClassDefinitionConfiguration configuration = new ClassDefinitionConfiguration();
        configuration.setNoArgsConstructor(false);
        configuration.setAllArgsConstructor(false);
        configuration.setGetter(false);
        configuration.setSetter(false);
        configuration.setToString(false);
        ClassDefinition classDefinition = new ClassDefinition(null,null, null, className, fieldDefinitions, configuration);
        classDefinition.addAnnotationDefinition(new AnnotationDefinition(Data.class,null ));
        classDefinition.addSuperInterface(Serializable.class.getName());
        return classDefinition;
    }
}
