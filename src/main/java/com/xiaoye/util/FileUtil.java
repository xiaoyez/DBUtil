package com.xiaoye.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtil {

    public static String getClassPath() {
        File file = new File("target\\classes");

        return file.getAbsolutePath();
    }

    public static String getSrcClassPath() {
        File file = new File("src\\main\\java");

        return file.getAbsolutePath();
    }

    public static String getResourcePath(){
        File file = new File("src\\main\\resources");

        return file.getAbsolutePath();
    }

    public static String[] getRootPath()
    {
        return new String[]{getClassPath(), getResourcePath()};
    }

    public static String resolveResourcePath(String path) throws FileResolveException {
        String actualPath = null;

        if (path.startsWith("/"))
        {
            path = path.replace("/","\\");
            String resourcePath = getResourcePath();
            actualPath = resourcePath + "\\" + path;
            return actualPath;
        }

        if ((path.startsWith("classpath:")))
        {
            path = path.substring("classpath:".length());
            int index = path.lastIndexOf(".");
            String fileType = path.substring(index);
            path = path.replaceAll("\\.","\\\\");
            path = path.substring(0,index);
            path += fileType;
            actualPath = getClassPath() + "\\" + path;
           return actualPath;
        }

        if (!(path.startsWith("/")) && !(path.startsWith("classpath:")))
        {
            path = path.replace("/","\\");
            actualPath = getClassPath() + "\\" + path;
            return actualPath;
        }
        throw  new FileResolveException("cannot resolve this path[" + path + "],please check your path is correct");
    }

    public static String resolveSourcePath(String path) throws FileResolveException {
        String actualPath = null;

        if (path.startsWith("/"))
        {
            path = path.replace("/","\\");
            String resourcePath = getResourcePath();
            actualPath = resourcePath + "\\" + path;
            return actualPath;
        }

        if ((path.startsWith("classpath:")))
        {
            path = path.substring("classpath:".length());
            int index = path.lastIndexOf(".");
            String fileType = path.substring(index);
            path = path.replaceAll("\\.","\\\\");
            path = path.substring(0,index);
            path += fileType;
            actualPath = getSrcClassPath() + "\\" + path;
            return actualPath;
        }

        if (!(path.startsWith("/")) && !(path.startsWith("classpath:")))
        {
            path = path.replace("/","\\");
            actualPath = getSrcClassPath() + "\\" + path;
            return actualPath;
        }
        throw  new FileResolveException("cannot resolve this path[" + path + "],please check your path is correct");

    }

    public static String getCurrentPath(Class clazz) throws IOException {
        File file = new File(clazz.getResource("").getPath());
        return file.getPath().replace("target\\classes","src\\main\\java");
    }

}
