package com.xiaoye.support;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractContainer<C> {

    protected Map<String,C> sourceMap = new HashMap<String, C>();

    protected AbstractContainer(){}

    public static AbstractContainer getInstance()
    {
        return null;
    }

    public void registry(String name, C c)
    {
        sourceMap.put(name,c);
    }

    public C get(String name)
    {
        return sourceMap.get(name);
    }

    public Object[] getValues()
    {
        return sourceMap.values().toArray();
    }

}
