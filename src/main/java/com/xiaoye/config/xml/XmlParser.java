package com.xiaoye.config.xml;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.xiaoye.creator.BeanClassCreator;
import com.xiaoye.support.ClassCreatorContainer;
import com.xiaoye.support.DbParserContainer;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import com.xiaoye.util.StringUtil;

import com.xy.parser.DbParser;
import com.xy.parser.DefaultDbParser;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class XmlParser {
    private String xmlPath;

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    @SneakyThrows
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
                parseDataSource(element);
                continue;
            }

            if (element.getName().equals(Tags.CLASS_CREATOR))
            {
                parseClassCreator(element);
                continue;
            }

            throw new XmlParserException("the element named '"+ Tags.ROOT_TAG +"' only can accept the child node named '"+ Tags.DATASOURCE +"' or '" + Tags.CLASS_CREATOR + "'\n");
        }

    }

    @SneakyThrows
    public void parseDataSource(Element datasourceElement) throws XmlParserException, IOException, FileResolveException {
        String name = datasourceElement.attributeValue(Tags.ID);
        List<Element> datasourceChildElements = datasourceElement.elements();

        String driver = null,url = null,username = null,password = null;
        Properties properties = null;
        // TODO: 2021/11/15 待优化
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
                    username = properties.getProperty(defaultDataSourcePropertyNames[2]);
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
                username = value;

            } else if (Tags.PASSWORD.equals(column)) {
                password = value;

            } else {
                throw new XmlParserException("cannot parse the column '"+column+"'");
            }
        }

        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL(url);
        ds.setUser(username);
        ds.setPassword(password);
        Class.forName(driver);
        DbParser dbParser = new DefaultDbParser(ds);
        DbParserContainer.getInstance().registry(name,dbParser);
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
        String datasourceRef = element.attributeValue(Tags.DATASOURCE_REF);


        DbParser dbParser = DbParserContainer.getInstance().get(datasourceRef);

        BeanClassCreator creator = new BeanClassCreator(dbParser,basePackage,path,prefix);

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

            File file = new File(xmlPath);
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
            return FileUtil.resolveResourcePath(path);
    }

}
