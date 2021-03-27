package com.xiaoye.classgenerator.defenition;

/**
 * @author xiaoye
 * @create 2021-03-13 18:26
 */
public abstract class AbstractDefinition implements Definition {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(open())
                .append(generate())
                .append(close());
        return builder.toString();
    }
}
