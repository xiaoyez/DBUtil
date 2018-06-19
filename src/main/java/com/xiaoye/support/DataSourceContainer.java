package com.xiaoye.support;

import java.util.HashMap;
import java.util.Map;

public class DataSourceContainer extends AbstractContainer<DataSource> {

    private static DataSourceContainer container = null;

    protected DataSourceContainer() {
        super();
    }

    public static DataSourceContainer getInstance()
    {
        if (container == null)
            container = new DataSourceContainer();
        return container;
    }

}
