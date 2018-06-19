package com.xiaoye.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbc {

    private DataSource source;

    public Dbc(String driver, String url, String user, String password) {
        this.source = new DataSource(driver,url,user,password);
    }

    public Dbc(DataSource source)
    {
        this.source = source;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(source.getDriver());
        Connection connection = DriverManager.getConnection(source.getUrl(),source.getUser(),source.getPassword());
        return connection;
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Dbc{" +
                "source=" + source.toString() +
                '}';
    }
}
