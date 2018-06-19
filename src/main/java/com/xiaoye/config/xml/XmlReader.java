package com.xiaoye.config.xml;

import com.xiaoye.creator.ClassCreator;
import com.xiaoye.support.ClassCreatorContainer;
import com.xiaoye.util.FileResolveException;
import com.xiaoye.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlReader {

    private XmlParser parser = new XmlParser();

    private String xml_path;

    public XmlReader(String xml_path) throws ParserConfigurationException, IOException, SAXException, XmlParserException, DocumentException, FileResolveException {
        xml_path = FileUtil.resolvePath(xml_path);
        this.xml_path = xml_path;
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        Document document = reader.read(new File(xml_path));
        parser.setXml_path(xml_path);
        parser.parse(document);

    }

    public ClassCreator[] getClassCreators()  {
        Object[] values = ClassCreatorContainer.getInstance().getValues();
        ClassCreator[] creators = new ClassCreator[values.length];
        for (int i = 0; i < creators.length; i++)
        {
            creators[i] = (ClassCreator) values[i];
        }

        return creators;
    }


}
