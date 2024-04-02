package com.jiawa.train.generator.server;

import com.jiawa.train.generator.util.DbUtil;
import com.jiawa.train.generator.util.Field;
import com.jiawa.train.generator.util.FreemarkerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    static String serverPath = "[module]/src/main/java/com/jiawa/train/[module]/";
    static String pomPath = "generator/pom.xml";
    //    vue界面的路径
    static String vuePath = "admin/src/views/main/";
    //    表示生成的界面是不是只读的界面，如果为false的话，会生成增删改查，如果为true，则只生成查
    static boolean readOnly = false;

    static String module = "";

    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
//        获取配置文件名
        String generatorPath = getGeneratorPath();
//        获取当前模块名
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println(module);
//        将路径替换成当前模块名
        serverPath = serverPath.replace("[module]", module);
//        通过pom.xml配置文件的路径，再获取自定义xml文件的路径
        Document read = new SAXReader().read("generator/" + generatorPath);
//        寻找table节点
        Node node = read.selectSingleNode("//table");
        System.out.println(node);
//        表的中文名
        Node tableName = node.selectSingleNode("@tableName");
//        获取domain实体
        Node domainObjectName = node.selectSingleNode("@domainObjectName");

//        为dbUtil设置数据源
        Node connectionURL = read.selectSingleNode("//@connectionURL");
        Node userId = read.selectSingleNode("//@userId");
        Node password = read.selectSingleNode("//@password");
        DbUtil.url = connectionURL.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();

        String Domain = domainObjectName.getText();
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        String do_main = tableName.getText().replaceAll("_", "-");
//        获取表的中文名
        String tableNameCn = DbUtil.getTableComment(tableName.getText());
//        获取所有的列
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());
        Set<String> javaType = getJavaType(fieldList);

//        组装参数
        HashMap<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("fieldList", fieldList);
        param.put("typeSet", javaType);
        param.put("readOnly", readOnly);
        System.out.println("组织参数：" + param);

        System.out.println(serverPath);
        gen(Domain, param, "service", "service");
        gen(Domain, param, "controller", "controller");
        gen(Domain, param, "req", "req");
        gen(Domain, param, "req", "query");
        gen(Domain, param, "resp", "queryResp");
        genVue(Domain, param);
    }

    /**
     * 自由选择生成controller还是service
     *
     * @param Domain
     * @param param
     * @param target
     * @throws Exception
     */
    private static void gen(String Domain, HashMap<String, Object> param, String packageName, String target) throws Exception {
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + packageName + "/";
        new File(toPath).mkdirs();
//        将开头转为大写
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
//        将名字合在一起
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }


    /**
     * 生成前端界面
     *
     * @param do_main
     * @param param
     */
    private static void genVue(String do_main, Map<String, Object> param) throws Exception {
        FreemarkerUtil.initConfig("myVue.ftl");
        new File(vuePath + module).mkdirs();
        String fileName = vuePath + module + "/" + do_main + ".vue";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    /**
     * 获取pom配置文件的路径
     *
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

    /**
     * 获取所有的java类型，并使用set去重
     *
     * @param fieldList
     * @return
     */
    private static Set<String> getJavaType(List<Field> fieldList) {
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
