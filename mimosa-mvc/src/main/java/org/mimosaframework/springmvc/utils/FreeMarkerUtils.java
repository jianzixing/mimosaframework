package org.mimosaframework.springmvc.utils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RequestUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class FreeMarkerUtils {

    public static void process(ModelObject root, String name, String template, Writer out) throws IOException, TemplateException {
        StringTemplateLoader loader = new StringTemplateLoader();
        loader.putTemplate(name, template);
        process(root, name, out, loader);
    }

    public static void process(ModelObject root, String name, Writer out, TemplateLoader loader) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setNumberFormat("#");
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        Template temp = cfg.getTemplate(name, "utf-8");
        temp.process(root, out);
    }

    public static String process(ModelObject root, String name, String template) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(root, name, template, writer);
        return writer.toString();
    }

    public static String process(ModelObject root, String name, TemplateLoader loader) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(root, name, writer, loader);
        return writer.toString();
    }

    public static void process(String relativeFilePath, String fileName, ModelObject root, Writer out) throws IOException, TemplateException {
        File file = RequestUtils.getWebAppRoot(relativeFilePath);
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        FileTemplateLoader loader = new FileTemplateLoader(file);
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        Template temp = cfg.getTemplate(fileName, "utf-8");
        temp.process(root, out);
    }

    public static String process(String relativeFilePath, String fileName, ModelObject root) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(relativeFilePath, fileName, root, writer);
        return writer.toString();
    }
}
