package com.jiawa.train.generator.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FreemarkerUtil {
    static String ftlPath = "generator/src/main/java/com/jiawa/train/generator/ftl/";

    static Template template;

    /**
     * 将freemark和ftl文件关联起来
     * @param ftlName
     * @throws IOException
     */
    public static void initConfig(String ftlName) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        template = cfg.getTemplate(ftlName);
    }

    /**
     * 将生成的类进行渲染
     * @param fileName
     * @param map
     * @throws Exception
     */
    public static void generator(String fileName, Map<String, Object> map) throws Exception {
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        template.process(map, bw);
        bw.flush();
        fw.close();
    }
}
