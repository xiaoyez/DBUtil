import com.xiaoye.config.xml.XmlParserException;
import com.xiaoye.config.xml.XmlReader;
import com.xiaoye.creator.BeanClassCreator;
import com.xiaoye.util.FileResolveException;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class MyTest {

    @Test
    public void test() throws XmlParserException, ParserConfigurationException, SAXException, DocumentException, IOException, SQLException, ClassNotFoundException, FileResolveException {
        XmlReader reader = new XmlReader("classpath:test.db-parser.xml");
        BeanClassCreator[] beanClassCreators = reader.getClassCreators();
        for (BeanClassCreator creator : beanClassCreators)
        {
            creator.createJavaFileSuiteTkMapper();

        }
    }

    @Test
    public void testGetBasePackage() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class clazz = BeanClassCreator.class;
        Method getPackageName = clazz.getDeclaredMethod("getPackageName", String.class);
        getPackageName.setAccessible(true);
        String path = "D:\\code\\JAVA\\DBUtil\\src\\main\\java\\com\\xiaoye\\creator\\BeanClassCreator.java";
        getPackageName.invoke(clazz.newInstance(),path);

    }
}
