package org.mimosaframework.core.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class LocalStorage {
    public static final String SUFFIX = ".mimosa";
    public static final String DEFAULT_NAME = "storage";
    private Properties properties = new Properties();
    private String path;
    private File file;

    public LocalStorage() throws IOException {
        String path = System.getProperty("user.dir");
        path = path + File.separator + DEFAULT_NAME + SUFFIX;
        File file = new File(path);
        this.file = file;
        this.init(file);
    }

    public LocalStorage(String path) throws IOException {
        this.path = path;
        if (path.toLowerCase().endsWith(SUFFIX.toLowerCase())) {
            file = new File(path);
        } else {
            file = new File(path + File.separator + DEFAULT_NAME + SUFFIX);
        }
        this.init(file);
    }

    private void init(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IllegalArgumentException("创建存储文件夹失败，请检查系统权限或者存储空间");
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IllegalArgumentException("创建存储文件失败，请检查系统权限或者存储空间");
            }
        }
    }

    public LocalStorage(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("存储文件必须是一个文件且以" + SUFFIX + "结尾");
        }
        this.file = file;
        this.init(file);
    }

    public void save(String key, String value) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
            properties.setProperty(key, value);
            outputStream = new FileOutputStream(file);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            properties.store(outputStream, format.format(new Date()) + " committed");
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public String get(String key) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }
}
