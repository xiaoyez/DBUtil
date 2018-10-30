package com.xiaoye.util;

public class StringUtil {

    public static boolean hasText(String str)
    {
        if (str != null && !str.equals(""))
            return true;
        return false;
    }

    public static String firstLetterToUpperCase(String str)
    {
        if (str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
        {
            str = str.replaceFirst(String.valueOf(str.charAt(0)),String.valueOf(str.charAt(0)).toUpperCase());
        }
        return str;
    }

    public static String castUnderlineToCamel(String str)
    {
        String[] strs = str.split("_");

        StringBuilder builder = new StringBuilder();
        for (String s : strs) {
            builder.append(firstLetterToUpperCase(s));
        }
        return builder.toString();
    }
}
