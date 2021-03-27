package com.xiaoye.creator;

import com.xiaoye.classgenerator.defenition.ClassDefinition;
import com.xiaoye.support.DataSource;
import com.xiaoye.support.DatabaseMetadata;
import com.xiaoye.support.Dbc;
import com.xiaoye.support.Table;
import com.xiaoye.util.ClassDefinitionUtil;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import com.xiaoye.util.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassCreator {

    private DataSource source;

    private String basePackage = null;

    private String path;

    private String prefix;

    private Map<String,String> classMapping = new HashMap<String, String>();


    public ClassCreator(){}

    public ClassCreator(DataSource source, String basePackage, String path, String prefix) {
        this.source = source;
        this.basePackage = basePackage;
        this.path = path;
        this.prefix = prefix;
    }


    public Map<String, String> getClassMapping() {
        return classMapping;
    }

    public void setClassMapping(Map<String, String> classMapping) {
        this.classMapping = classMapping;
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

        //类与表的映射

        mappingClassAndTable(tables,classMapping);

        File directory =  createDirectory(path);
        for (Table table : tables)
        {
            createJavaFile(directory.getAbsolutePath(),table,prefix);
        }
        connection.close();
    }

    @SneakyThrows
    private File createDirectory(String path) {
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
        return directory;
    }

    private void mappingClassAndTable(List<Table> tables, Map<String, String> classMapping) {
        Set<String> tableNames = classMapping.keySet();
        for (String tableName : tableNames)
        {
            for (Table table : tables)
            {
                if (table.getName().equals(tableName))
                {
                    String className = classMapping.get(tableName);
                    table.setName(className);
                }
            }
        }
    }


    private String castPackageToPath(String basePackage)
    {
        basePackage = basePackage.replace(".","\\");
        basePackage = FileUtil.getClassPath() + "\\" + basePackage;
        return basePackage;
    }

    public void createJavaFile(String path, Table table, String prefix) throws IOException {
        File directory = new File(path);
        checkDirectory(directory);

        ClassDefinition classDefinition = ClassDefinitionUtil.fromTable(table);

        deletePrefix(prefix,classDefinition);

        File javaFile = createJavaFileByPath(path,classDefinition);

        basePackage = getPackageName(javaFile);
        classDefinition.setPackageName(basePackage);

        String content = classDefinition.toString();
        FileUtils.writeStringToFile(javaFile, content, "UTF-8");

    }

    private String getPackageName(File javaFile) {
        if (!StringUtil.hasText(basePackage))
        {
            basePackage = getPackageName(javaFile.getAbsolutePath());
        }
        return basePackage;
    }

    private File createJavaFileByPath(String path,ClassDefinition classDefinition) {
        if (!path.endsWith("\\") )
        {
            path += "\\";
        }
        String fileName = path + classDefinition.getName() + ".java";
        File javaFile = new File(fileName);
        if (javaFile.exists())
        {
            return null;
        }

//        System.out.println(javaFile.getAbsolutePath());
        try {
            javaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return javaFile;
    }

    private void deletePrefix(String prefix, ClassDefinition classDefinition) {

        if (prefix != null)
        {
            if (classDefinition.getName().startsWith(prefix))
                classDefinition.setName(classDefinition.getName().substring(prefix.length()));
        }
    }

    private void checkDirectory(File directory) {
        if (!directory.exists())
            directory.mkdirs();

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
                ", classMapping=" + classMapping +
                '}';
    }
}
