package com.xiaoye.config.xml;

import com.xiaoye.creator.BeanClassCreator;
import com.xiaoye.support.ClassCreatorContainer;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import com.xy.parser.DbParser;
import lombok.Getter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlReader {

    private XmlParser parser = new XmlParser();

    @Getter
    private String xmlPath;

    public XmlReader(String xmlPath) throws ParserConfigurationException, IOException, SAXException, XmlParserException, DocumentException, FileResolveException {
        xmlPath = FileUtil.resolveResourcePath(xmlPath);
        this.xmlPath = xmlPath;
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        Document document = reader.read(new File(xmlPath));
        parser.setXmlPath(xmlPath);
        parser.parse(document);

    }

    public BeanClassCreator[] getClassCreators()  {
        Object[] values = ClassCreatorContainer.getInstance().getValues();
        BeanClassCreator[] creators = new BeanClassCreator[values.length];
        for (int i = 0; i < creators.length; i++)
        {
            creators[i] = (BeanClassCreator) values[i];
        }

        return creators;
    }


}
