package com.xiaoye.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Table {

    private String name;
    private List<Column> columns = new ArrayList<Column>();

    public Table()
    {

    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void addColumn(Column column)
    {
        this.columns.add(column);
    }

    public void addAllColumns(Collection<Column> columns)
    {
        this.columns.addAll(columns);
    }

    public void addAllColumns(Column[] columns)
    {
        this.columns.addAll(Arrays.asList(columns));
    }

    public Column removeColumn(String name)
    {
        for (Column c : columns)
        {
            if (c.equals(name))
            {
                columns.remove(c);
                return c;
            }
        }
        return null;
    }

    public static class Column{
        public String name;
        public Class type;

        public Column(String name, Class type) {
            this.name = name;
            this.type = type;
        }
    }
}
