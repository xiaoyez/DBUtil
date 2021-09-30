package com.xiaoye.creator;

import com.xiaoye.classgenerator.constant.Modifer;
import com.xiaoye.classgenerator.defenition.type.InterfaceDefinition;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author xiaoye
 * @create 2021-04-01 15:09
 */
public class MapperCreator {

    @SneakyThrows
    public static void generateTkMapper(String packageName, String beanClassName)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("package ")
                .append(packageName)
                .append(";\n\n")
                .append("import tk.mybatis.mapper.common.Mapper;\n")
                .append("import test.bean." + beanClassName + ";\n\n")
                .append("public interface " + beanClassName + "TkMapper ")
                .append("extends Mapper<" + beanClassName + ">{\n\n")
                .append("}");
        String path = "D:\\code\\java\\DBUtil\\src\\main\\java\\test\\mapper";
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs();
        File mapperFile = new File(path, beanClassName + "TkMapper.java");
        if (mapperFile.exists())
            mapperFile.delete();
        mapperFile.createNewFile();
        FileUtils.writeStringToFile(mapperFile,builder.toString(),"UTF-8");

    }
}
