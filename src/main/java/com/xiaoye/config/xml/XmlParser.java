package com.xiaoye.config.xml;

import com.xiaoye.creator.ClassCreator;
import com.xiaoye.support.ClassCreatorContainer;
import com.xiaoye.support.DataSource;
import com.xiaoye.support.DataSourceContainer;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import com.xiaoye.util.StringUtil;

import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class XmlParser {
    private String xml_path;

    public String getXml_path() {
        return xml_path;
    }

    public void setXml_path(String xml_path) {
        this.xml_path = xml_path;
    }

    public void parse(Document document) throws XmlParserException, FileResolveException {
        Element root_element = document.getRootElement();
        if (!root_element.getName().equals(Tags.ROOT_TAG))
        {
            throw new XmlParserException("the root element must named '"+ Tags.ROOT_TAG +"'");
        }
        List<Element> elements = root_element.elements();
        for (Element element : elements)
        {
            if (element.getName().equals(Tags.DATASOURCE))
            {
                try {
                    parseDataSource(element);
                    continue;
                } catch (XmlParserException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (element.getName().equals(Tags.CLASS_CREATOR))
            {
                parseClassCreator(element);
                continue;
            }

            throw new XmlParserException("the element named '"+ Tags.ROOT_TAG +"' only can accept the child node named '"+ Tags.DATASOURCE +"' or '" + Tags.CLASS_CREATOR + "'\n");
        }

    }

    public void parseDataSource(Element datasourceElement) throws XmlParserException, IOException, FileResolveException {
        String name = datasourceElement.attributeValue(Tags.ID);
        List<Element> datasourceChildElements = datasourceElement.elements();

        String driver = null,url = null,user = null,password = null;
        Properties properties = null;
        for (Element propertyElement  : datasourceChildElements)
        {
            if (!propertyElement.getName().equals(Tags.PROPERTY))
            {
                Element locationElement = propertyElement;
                if (!locationElement.getName().equals(Tags.LOCATION))
                    throw new XmlParserException("the element named 'data-source' only can accept the child node named 'location' or 'property'\n");
                else
                {
                    properties = new Properties();
                    properties.load(new FileInputStream(resolvePath(locationElement.getText())));
                    String[] defaultDataSourcePropertyNames = {"datasource.driver","datasource.url","datasource.user","datasource.password"};
                    driver = properties.getProperty(defaultDataSourcePropertyNames[0]);
                    url = properties.getProperty(defaultDataSourcePropertyNames[1]);
                    user = properties.getProperty(defaultDataSourcePropertyNames[2]);
                    password = properties.getProperty(defaultDataSourcePropertyNames[3]);
                    continue;
                }
            }



            String column = propertyElement.attributeValue(Tags.COLUMN);
            String value = propertyElement.attributeValue(Tags.VALUE);
            if (Tags.DRIVER.equals(column)) {
                driver = value;

            } else if (Tags.URL.equals(column)) {
                url = value;

            } else if (Tags.USER.equals(column)) {
                user = value;

            } else if (Tags.PASSWORD.equals(column)) {
                password = value;

            } else {
                throw new XmlParserException("cannot parse the column '"+column+"'");
            }
        }
        DataSource dataSource = new DataSource(driver,url,user,password);
        DataSourceContainer container =  DataSourceContainer.getInstance();
        container.registry(name,dataSource);


    }

    public void parseClassCreator(Element element)
    {
        String name = element.attributeValue(Tags.ID);
        String basePackage = element.attributeValue(Tags.BASE_PACKAGE);
        String path = null;
        if (!StringUtil.hasText(basePackage))
        {
            path = element.attributeValue(Tags.PATH);
        }

        String prefix = element.attributeValue(Tags.PREFIX);
        String data_source_ref = element.attributeValue(Tags.DATASOURCE_REF);

        DataSource dataSource = DataSourceContainer.getInstance().get(data_source_ref);

        ClassCreator creator = new ClassCreator(dataSource,basePackage,path,prefix);

        List<Element> classMappingElements = element.elements("class-mapping");
        for (Element classMappingElement : classMappingElements)
        {
            String tableName = classMappingElement.attributeValue("table-name");
            String className = classMappingElement.attributeValue("class-name");
            creator.getClassMapping().put(tableName,className);
        }

        ClassCreatorContainer.getInstance().registry(name,creator);
    }

    private String resolvePath(String path) throws FileNotFoundException, FileResolveException {
        if (path.startsWith("./") || (!path.startsWith(".") && !path.startsWith("/")))
        {
            String actualPath = null;

            path = path.replace("./", "");
            int index = path.lastIndexOf(".");
            String fileType = path.substring(index);
            path = path.replaceAll("\\.","\\\\");
            path = path.substring(0,index);
            path += fileType;

            File file = new File(xml_path);
            actualPath = file.getParentFile().getAbsolutePath();
            if (file.exists())
            {
                actualPath = actualPath + "\\" +  path;
                return actualPath;
            }
            else
                throw new FileNotFoundException("No such file or directory, please check the path:" + actualPath);
        }
        else
            return FileUtil.resolvePath(path);
    }

}
