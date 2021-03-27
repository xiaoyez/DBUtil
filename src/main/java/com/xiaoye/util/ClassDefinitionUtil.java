package com.xiaoye.util;

import com.xiaoye.classgenerator.config.ClassGeneratorConfiguration;
import com.xiaoye.classgenerator.constant.Modifer;
import com.xiaoye.classgenerator.defenition.ClassDefinition;
import com.xiaoye.classgenerator.defenition.FieldDefinition;
import com.xiaoye.classgenerator.util.ArrayList;
import com.xiaoye.support.Table;

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

        return new ClassDefinition(Modifer.PUBLIC,null , className,fieldDefinitions,new ClassGeneratorConfiguration());
    }
}
