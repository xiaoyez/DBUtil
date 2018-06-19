package com.xiaoye.support;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseMetadata {

    private Connection conn;

    public DatabaseMetadata(Connection conn)
    {
        this.conn = conn;
    }

    public List<Table> getTables() throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet tables = databaseMetaData.getTables(conn.getCatalog(), "root", null, new String[]{"table"});
        List<Table> tableList = new ArrayList<Table>();
        while (tables.next())
        {
            String table_name = tables.getString("TABLE_NAME");
            Table table = new Table(table_name);
            ResultSet columns = databaseMetaData.getColumns(conn.getCatalog(), "root", table_name, null);
            while (columns.next())
            {
                String column_name = columns.getString("COLUMN_NAME");
                int data_type = columns.getInt("DATA_TYPE");
                Class clazz = getType(data_type);
                Table.Column column = new Table.Column(column_name,clazz);
                table.addColumn(column);
            }
            tableList.add(table);
        }
        return tableList;
    }

    private Class getType(int sql_type)
    {
        switch (sql_type)
        {
            case Types.INTEGER:
                return Integer.class;
            case Types.VARCHAR:
                return String.class;
            case Types.FLOAT:
                return Float.class;
            case Types.DATE:
                return Date.class;
            case Types.TIME:
                return Date.class;
            default:
                return String.class;
        }
    }


    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
