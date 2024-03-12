package com.jiawa.train.generator.server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;

public class ServerGenerator {
    static String toPath = "generator/src/main/java/com/jiawa/train/generator/test/";
    static String pomPath = "generator/pom.xml";

    static {
        new File(toPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        String generatorPath = getGeneratorPath();
//        通过pom.xml配置文件的路径，再获取自定义xml文件的路径
        Document read = new SAXReader().read("generator/" + generatorPath);
//        寻找table节点
        Node node = read.selectSingleNode("//table");
        System.out.println(node);
        Node tableName = node.selectSingleNode("@tableName");
//        获取domain实体
        Node domainObjectName = node.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText()+"/"+domainObjectName.getText());
    }

    /**
     * 获取pom配置文件的路径
     * @return
     * @throws DocumentException
     */
    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
//        读取定义好的文件，获取到的document对象就是整个XML
        Document read = saxReader.read(pomPath);
//        使用XPath快速定位节点或属性,//从根目录下寻找，pom是xml命名空间，configurationFile是节点名，如果要找属性，则是@XXX
        Node node = read.selectSingleNode("//pom:configurationFile");
        return node.getText();
    }
//    public static void main(String[] args) throws Exception {
////      读取ftl文件模板
//        FreemarkerUtil.initConfig("test.ftl");
//        HashMap<String, Object> param = new HashMap<>();
//        param.put("domain", "Test");
////        渲染java程序
//        FreemarkerUtil.generator(toPath + "Test.java", param);
//    }
}
