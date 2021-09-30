package com.xiaoye.support;

import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
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
                String comment = columns.getString("REMARKS");
                column.comment = comment;
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
            case Types.TIMESTAMP:
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

    @SneakyThrows
    public List<Table> getViews(String dbName) {
        List<Table> tables = new ArrayList<>();
        Statement statement = conn.createStatement();
        String sql = "select `TABLE_NAME` from information_schema.TABLES where table_type='view' and TABLE_SCHEMA ='"+dbName+"';";
        ResultSet resultSet = statement.executeQuery(sql);
        while (!resultSet.isClosed() && resultSet.next())
        {
            String viewName = resultSet.getString("TABLE_NAME");
            Table table = new Table();
            table.setName(viewName);
            sql = "select * from " + viewName;
            Statement statement1 = conn.createStatement();
            ResultSet viewResultSet = statement1.executeQuery(sql);
            ResultSetMetaData metaData = viewResultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);
                Table.Column column = new Table.Column(columnName, getType(columnType));
                table.addColumn(column);
            }
            tables.add(table);
            statement1.close();
        }
        return tables;
    }
}
