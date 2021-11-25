package com.xiaoye.creator;

import com.xiaoye.classgenerator.defenition.type.ClassDefinition;
import com.xiaoye.util.ClassDefinitionUtil;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import com.xiaoye.util.StringUtil;
import com.xy.entity.DB;
import com.xy.entity.Table;
import com.xy.parser.DbParser;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Data
public class BeanClassCreator {

    private DbParser dbParser;

    private Connection connection;

    private String basePackage = null;

    private String path;

    private String prefix;

    private Map<String,String> classMapping = new HashMap<String, String>();


    public BeanClassCreator(){}

    public BeanClassCreator(DbParser dbParser, String basePackage, String path, String prefix) {
        this.dbParser = dbParser;
        this.basePackage = basePackage;
        this.path = path;
        this.prefix = prefix;
        this.connection = dbParser.getConnection();
    }

    public BeanClassCreator(DbParser dbParser) {
        this.dbParser = dbParser;
    }

    public void createJavaFile() throws SQLException, ClassNotFoundException, IOException, FileResolveException {
        Collection<Table> tables = getTables();
        File directory =  createDirectory(path);
        for (Table table : tables)
        {
            createJavaFile(directory.getAbsolutePath(),table,prefix);
        }
        connection.close();
    }

    public void createJavaFileSuiteTkMapper() throws SQLException, ClassNotFoundException, IOException {

        Collection<Table> tables = getTables();

        File directory =  createDirectory(path);
        for (Table table : tables)
        {
            createJavaFileSuiteTkMapper(directory.getAbsolutePath(),table,prefix);
        }
        if (!connection.isClosed())
            connection.close();
    }

    public Collection<Table> getTables() throws SQLException, ClassNotFoundException {

        DB db = dbParser.parse();
        Collection<Table> tables = db.getTables();
        mappingClassAndTable(tables,classMapping);
        return tables;
    }

    private void createJavaFileSuiteTkMapper(String path, Table table, String prefix) throws IOException {

        ClassDefinition classDefinition = getClassDefinition(table,prefix,true);
        classDefinition.setPackageName(basePackage);
        createJavaFile(classDefinition,path);
    }

    private void createJavaFile(ClassDefinition classDefinition, String path) throws IOException {
        File directory = new File(path);
        checkDirectory(directory);

        File javaFile = createJavaFileByPath(path,classDefinition);

        basePackage = getPackageName(javaFile);
        classDefinition.setPackageName(basePackage);

        String content = classDefinition.toString();
        FileUtils.writeStringToFile(javaFile, content, "UTF-8");
        MapperCreator.generateTkMapper(basePackage.replace("bean","mapper"),classDefinition.getName());
    }

    private ClassDefinition getClassDefinition(Table table, String prefix, boolean tkMapper) {
        ClassDefinition classDefinition = null;
        if (tkMapper)
        {
            classDefinition  = ClassDefinitionUtil.fromTableSuiteTkMapper(table);
        }
        else
        {
            classDefinition = ClassDefinitionUtil.fromTable(table);
        }
        deletePrefix(prefix,classDefinition);
        return classDefinition;
    }

    @SneakyThrows
    private File createDirectory(String path) {
        File directory;
        if (StringUtil.hasText(path))
        {
            directory = new File(FileUtil.resolveSourcePath(path));
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

    private void mappingClassAndTable(Collection<Table> tables, Map<String, String> classMapping) {
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
        basePackage = FileUtil.getSrcClassPath() + "\\" + basePackage;
        return basePackage;
    }

    public void createJavaFile(String path, Table table, String prefix) throws IOException {
        ClassDefinition classDefinition = getClassDefinition(table, path, false);
        createJavaFile(classDefinition, path);
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

}
