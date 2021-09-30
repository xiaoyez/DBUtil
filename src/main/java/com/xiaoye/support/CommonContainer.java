package com.xiaoye.support;

import java.util.HashMap;

/**
 * @author xiaoye
 * @create 2021-04-01 15:11
 */
public class CommonContainer extends HashMap<String,Object> {

    private static CommonContainer container = new CommonContainer();

    public static void register(String name,Object obj)
    {
        container.put(name,obj);
    }

    public static Object get(String name)
    {
        return container.get(name);
    }
}
