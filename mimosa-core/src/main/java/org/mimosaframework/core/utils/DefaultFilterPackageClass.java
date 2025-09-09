package org.mimosaframework.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * {@link FilterPackageClass}接口的默认实现
 * 查找时是获得当前线程的类加载器查找
 *
 * @author yangankang
 */
public class DefaultFilterPackageClass implements FilterPackageClass {

    /**
     * 设置默认的类加载器
     */
    private ClassLoader classLoader = DefaultFilterPackageClass.class.getClassLoader();

    /**
     * 查找类的包的名称
     */
    private List<String> packagePaths = null;

    public DefaultFilterPackageClass() {
    }

    public DefaultFilterPackageClass(String packagePath) {
        this();
        this.packagePaths = new ArrayList<String>();
        this.packagePaths.add(packagePath);
    }

    /**
     * 通过包名称和注解类过滤查找类
     *
     * @param annotationClass 注解类
     * @return
     */
    @Override
    public Set<Class> getScanClass(Class<? extends Annotation> annotationClass) {

        if (packagePaths == null || packagePaths.isEmpty()) {
            throw new IllegalArgumentException("查找类时包信息不能为空");
        }

        if (annotationClass == null) {
            throw new IllegalArgumentException("查找类时注解类不能为空");
        }

        Set<Class> classes = new LinkedHashSet<>();

        for (String packagePath : this.getPackages()) {
            if (StringTools.isNotEmpty(packagePath)) {
                //获取包下的所有类
                List<Class<?>> allClasses = getClasses(packagePath);

                //过滤annotationClass注解的类
                if (allClasses != null) {
                    for (Class<?> clazz : allClasses) {
                        if (isAnnotationDeclaredLocally(annotationClass, clazz)) {
                            classes.add(clazz);
                        }
                    }
                }
            }
        }
        if (!classes.isEmpty()) {
            return classes;
        }
        return null;
    }

    public boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz) {
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType不能为空");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz不能为空");
        }
        try {
            for (Annotation ann : clazz.getDeclaredAnnotations()) {
                if (ann.annotationType() == annotationType) {
                    return true;
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 查找一组注解相关的类
     * 同{@link #getScanClass(Class)}
     *
     * @param annotationClass
     * @return
     */
    @Override
    public Set<Class> getScanClass(Class<? extends Annotation>[] annotationClass) {

        Set<Class> set = null;

        for (Class<? extends Annotation> c : annotationClass) {
            if (set != null) {
                set.addAll(this.getScanClass(c));
            } else {
                set = this.getScanClass(c);
            }
        }
        return null;
    }

    /**
     * 和方法 {@link #getScanClass(Class)}方法一样
     * 获得Class之后初始化无参构造实例
     *
     * @param annotationClass 注解类
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public Set getScanClassAndInstance(Class<? extends Annotation> annotationClass) throws IllegalAccessException, InstantiationException {
        Set<Class> classes = this.getScanClass(annotationClass);
        Set instanceClasses = new HashSet();
        for (Class clazz : classes) {
            instanceClasses.add(clazz.newInstance());
        }
        return instanceClasses;
    }

    /**
     * 设置默认的类加载器
     *
     * @param classLoader 类加载器实现
     */
    @Override
    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader != null) {
            this.classLoader = classLoader;
        }
    }

    /**
     * 设置查找的包的路径名称
     *
     * @param packagePath
     */
    @Override
    public void setPackagePath(List<String> packagePath) {
        if (packagePath != null) {
            List<String> list = new ArrayList<>();
            for (String s : packagePath) {
                if (StringTools.isNotEmpty(s)) {
                    list.add(s.trim());
                }
            }
            this.packagePaths = list;
        }
    }

    /**
     * 返回查找的包的路径名称
     *
     * @return 包路径
     */
    @Override
    public List<String> getPackagePath() {
        return this.packagePaths;
    }

    /**
     * 判断包是否存在
     *
     * @return
     */
    @Override
    public boolean hasResource() {
        return (this.packagePaths != null && this.packagePaths.size() > 0) ? true : false;
    }

    /**
     * 查找类分为两种情况一个是已经打包的jar一个是.class文件类
     * 通过当前线程的上下文类加载器 Thread.currentThread().getContextClassLoader().getResources 获得资源
     *
     * @param packageName 包名称
     * @return 包下的所有类
     */
    private List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        boolean recursive = true;

        if (packageName == null || packageName.equals("")) {
            throw new IllegalArgumentException("查找类时包信息不能为空");
        }

        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            //获得包下的所有资源文件
            dirs = classLoader.getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                //通过资源协议判断是否是类文件
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        //从jar包中获取所有的.class文件
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 循环遍历文件下的所有class文件并且获取后加入列表
     *
     * @param packageName 包名称
     * @param packagePath 包路径
     * @param recursive   是否遍历文件夹
     * @param classes     查找结果
     */
    private void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        //过滤文件和文件夹
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        //递归循环获取所有的.class文件，将已经获得class加入列表
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private List<String> getPackages() {
        List<String> list = new ArrayList<String>();
        for (String packagePath : packagePaths) {
            List<String> pkgs = this.getSubMatchPackages(packagePath);
            if (pkgs != null && !pkgs.isEmpty()) {
                list.addAll(pkgs);
            }
        }
        return list;
    }

    private List<String> getSubMatchPackages(String packageName) {
        String[] packageDirNames = packageName.split("\\.");
        StringBuilder prefixPackage = new StringBuilder();
        for (String packageDirName : packageDirNames) {
            if ("*".equals(packageDirName) || "**".equals(packageDirName)) {
                List<String> pkgs = new ArrayList<>();
                List<String> subs = this.getSubPackages(prefixPackage.toString());
                if (subs != null && !subs.isEmpty()) {
                    for (String sub : subs) {
                        List<String> list = this.getSubMatchPackages(sub);
                        if (list != null) pkgs.addAll(list);
                    }
                }
                return pkgs;
            } else {
                if (!prefixPackage.isEmpty()) {
                    prefixPackage.append(".");
                }
                prefixPackage.append(packageDirName);
            }
        }
        return List.of(packageName);
    }

    private List<String> getSubPackages(String packageName) {
        List<String> pkgs = new ArrayList<>();
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new IllegalArgumentException("查找类时包信息不能为空");
        }

        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            //获得包下的所有资源文件
            dirs = classLoader.getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                //通过资源协议判断是否是类文件
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    File dir = new File(filePath);
                    if (dir.exists() && dir.isDirectory()) {
                        File[] dirFiles = dir.listFiles(File::isDirectory);
                        if (dirFiles != null) {
                            for (File file : dirFiles) {
                                String className = file.getName();
                                pkgs.add(packageName + '.' + className);
                            }
                        }
                    }
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        //从jar包中获取所有的.class文件
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName) && entry.isDirectory()) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                pkgs.add(packageName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pkgs;
    }
}
