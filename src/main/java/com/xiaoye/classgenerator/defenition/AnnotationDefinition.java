package com.xiaoye.classgenerator.defenition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaoye
 * @create 2021-03-13 18:33
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationDefinition extends AbstractDefinition {

    private Class annotation;

    private Map<String,Object> paramMap = null;

    public void addParam(String name,String value)
    {
        if (paramMap == null)
            paramMap = new HashMap<>();
        paramMap.put(name, value);
    }

    public String open() {
        return "@";
    }

    public String close() {
        return "\n";
    }

    public String generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(annotation.getName());
        if (paramMap != null)
        {
            builder.append("(");
            Set<Map.Entry<String, Object>> paramEntries = paramMap.entrySet();
            for (Map.Entry<String, Object> paramEntry : paramEntries) {
                builder.append(paramEntry.getKey())
                        .append("=")
                        .append("\""+paramEntry.getValue()+"\"");
            }
            builder.append(")");
        }
        return builder.toString();
    }
}
