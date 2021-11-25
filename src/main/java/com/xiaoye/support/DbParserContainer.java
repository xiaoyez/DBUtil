package com.xiaoye.support;

import com.xy.parser.DbParser;

/**
 * @author xiaoye
 * @create 2021-11-15 0:51
 */
public class DbParserContainer extends AbstractContainer<DbParser>{

    private static DbParserContainer container = null;

    protected DbParserContainer() {
        super();
    }

    public static DbParserContainer getInstance()
    {
        if (container == null)
            container = new DbParserContainer();
        return container;
    }
}
