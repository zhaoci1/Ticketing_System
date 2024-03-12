package com.jiawa.train.generator.server;

import org.dom4j.Document;
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
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom","http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
//        读取定义好的文件，获取到的document对象就是整个XML
        Document read = saxReader.read(pomPath);
//        使用XPath快速定位节点或属性,//从根目录下寻找，pom是xml命名空间，configurationFile是节点名，如果要找属性，则是@XXX
        Node node =  read.selectSingleNode("//pom:configurationFile");
        System.out.println(node);
        System.out.println(node.getText());
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
