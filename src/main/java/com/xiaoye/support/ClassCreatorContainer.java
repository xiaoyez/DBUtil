package com.xiaoye.support;

import com.xiaoye.creator.BeanClassCreator;

public class ClassCreatorContainer extends AbstractContainer<BeanClassCreator> {

    private static ClassCreatorContainer container = null;

    protected ClassCreatorContainer() {
        super();
    }

    public static ClassCreatorContainer getInstance()
    {
        if (container == null)
            container = new ClassCreatorContainer();
        return container;
    }
}
