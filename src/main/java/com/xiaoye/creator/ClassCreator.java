package com.xiaoye.creator;

import com.xiaoye.support.DataSource;
import com.xiaoye.support.DatabaseMetadata;
import com.xiaoye.support.Dbc;
import com.xiaoye.support.Table;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import com.xiaoye.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClassCreator {

    private DataSource source;

    private String basePackage = null;

    private String path;

    private String prefix;


    public ClassCreator(){}

    public ClassCreator(DataSource source, String basePackage, String path, String prefix) {
        this.source = source;
        this.basePackage = basePackage;
        this.path = path;
        this.prefix = prefix;
    }


    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ClassCreator(DataSource source) {
        this.source = source;
    }

    public void createJavaFile() throws SQLException, ClassNotFoundException, IOException, FileResolveException {
        Dbc dbc = new Dbc(source);
        Connection connection = dbc.getConnection();
        DatabaseMetadata databaseMetadata = new DatabaseMetadata(connection);
        List<Table> tables = databaseMetadata.getTables();

        File directory;
        if (StringUtil.hasText(path))
        {
            directory = new File(FileUtil.resolvePath(path));
            if (!directory.exists())
                directory.mkdirs();
        }
        else
        {
            directory = new File(castPackageToPath(basePackage));
            if (!directory.exists())
                directory.mkdirs();
        }

        for (Table table : tables)
        {
            createJavaFile(directory.getAbsolutePath(),table,prefix);
        }

        connection.close();
    }


    private String castPackageToPath(String basePackage)
    {
        basePackage = basePackage.replace(".","\\");
        basePackage = FileUtil.getClassPath() + "\\" + basePackage;
        return basePackage;
    }

    public void createJavaFile(String path, Table table, String prefix) throws IOException {
        File directory = new File(path);
        if (!directory.exists())
            directory.mkdirs();

        String className = table.getName();

        if (className.startsWith(prefix))
            className = className.substring(prefix.length());

        //首字母大写
        className = StringUtil.firstLetterToUpperCase(className);

        if (!path.endsWith("\\") )
        {
            path += "\\";
        }
        String fileName = path + className + ".java";
        File javaFile = new File(fileName);
        if (javaFile.exists())
        {
            return;
        }

//        System.out.println(javaFile.getAbsolutePath());
        javaFile.createNewFile();

        StringBuilder builder = new StringBuilder();


        if (!StringUtil.hasText(basePackage))
        {
            basePackage = getPackageName(javaFile.getAbsolutePath());
        }
        String packageName = basePackage;

        builder.append("package " + packageName + ";\n");

//        System.out.println(packageName);

        builder.append("\n");

        builder.append("public class ");
        builder.append(className);
        builder.append("{\n");

        //添加属性
        for (Table.Column column : table.getColumns())
        {
            String fieldName = column.name;
            String type = column.type.getName();

            builder.append("\t");
            builder.append("private ");
            builder.append(type + " ");
            builder.append(fieldName + ";\n");
        }
        builder.append("\n");

        //无参构造器
        builder.append("\t");
        builder.append("public ");
        builder.append(className);
        builder.append("(){}\n");

        builder.append("\n");


        //有参构造器
        builder.append("\t");
        builder.append("public ");
        builder.append(className);
        builder.append("(");

        //参数
        for (Table.Column column : table.getColumns())
        {
            String fieldName = column.name;
            String type = column.type.getName();

            builder.append(type + " ");
            builder.append(fieldName + ",");
        }

        //删除最后一个逗号
        builder.deleteCharAt(builder.length() - 1);

        builder.append("){\n");

        //this.xxx = xxx;
        for (Table.Column column : table.getColumns())
        {
            builder.append("\t\t");
            String fieldName = column.name;

            builder.append("this." + fieldName + " = " + fieldName + ";\n");
        }

        builder.append("\t}");

        builder.append("\n");

        //getter
        for (Table.Column column : table.getColumns())
        {
            String fieldName = column.name;
            String type = column.type.getName();

            builder.append("\t");
            builder.append("public ");
            builder.append(type + " ");
            builder.append("get");

            String methedName = new String(fieldName);
            if(fieldName.charAt(0) >= 'a' && fieldName.charAt(0) <= 'z')
            {
                methedName.replace(methedName.charAt(0), Character.toUpperCase(methedName.charAt(0)));
            }

            builder.append(methedName);
            builder.append("(){return " + fieldName + ";}\n");
            builder.append("\n");
        }


        //setter
        for (Table.Column column : table.getColumns())
        {
            String fieldName = column.name;
            String type = column.type.getName();

            builder.append("\t");
            builder.append("public ");
            builder.append("void ");
            builder.append("set");

            String methedName = new String(fieldName);
            if(fieldName.charAt(0) >= 'a' && fieldName.charAt(0) <= 'z')
            {

                methedName.replace(methedName.charAt(0), Character.toUpperCase(methedName.charAt(0)));
            }

            builder.append(methedName);
            builder.append("(");
            builder.append(type + " " + fieldName);
            builder.append("){ this." + fieldName + " = " + fieldName + ";}\n");
            builder.append("\n");
        }

        builder.append("}");

        String content = builder.toString();
        PrintWriter writer = new PrintWriter(javaFile);
        writer.write(content);
        writer.close();


    }

    private String getPackageName(String path)
    {
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        String packageName = absolutePath.substring(FileUtil.getClassPath().length() + 1);
        packageName = packageName.replaceAll("\\\\","\\.");
        int index = packageName.lastIndexOf(".");
        packageName = packageName.substring(0,index);
        index = packageName.lastIndexOf(".");
        packageName = packageName.substring(0,index);


        return packageName;
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ClassCreator{" +
                "source=" + source +
                ", basePackage='" + basePackage + '\'' +
                ", path='" + path + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
