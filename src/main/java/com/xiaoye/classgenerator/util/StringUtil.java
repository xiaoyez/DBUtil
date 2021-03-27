package com.xiaoye.classgenerator.util;

/**
 * @author xiaoye
 * @create 2021-03-16 1:53
 */
public class StringUtil {

    public static String fromClass(Class clazz){
        String name = clazz.getName();
        if (name.startsWith("java.lang"))
            name = name.substring("java.lang.".length());
        return name;
    }

    public static String firstLetterToUpper(String str) {
        String substring = str.substring(1);
        return str.substring(0,1).toUpperCase()+substring;
    }
}
