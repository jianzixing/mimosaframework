package org.mimosaframework.core.utils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * 在指定包中过滤查找指定类型的类
 * 查找的类必须是同一个类加载器(默认的是当前线程的类加载器)
 *
 * @author yangankang
 */
public interface FilterPackageClass {

    /**
     * 通过包名称和类注解获得一个class集合
     *
     * @param annotationClass 注解类
     * @return
     */
    Set<Class> getScanClass(Class<? extends Annotation> annotationClass);

    /**
     * 同{@link #getScanClass(Class)}
     * 可以一次查找多种注解类
     *
     * @param annotationClass
     * @return
     */
    Set<Class> getScanClass(Class<? extends Annotation>... annotationClass);

    /**
     * 通过包名称和类注解获得一个class集合实例
     * 调用这个方法的要求是class实例必须有一个无参构造函数
     *
     * @param annotationClass 注解类
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    Set getScanClassAndInstance(Class<? extends Annotation> annotationClass) throws IllegalAccessException, InstantiationException;

    /**
     * 设置默认的类加载器
     *
     * @param classLoader 类加载器实现
     */
    void setClassLoader(ClassLoader classLoader);

    /**
     * 设置当前的类过滤器的包路径
     *
     * @param packagePath
     */
    void setPackagePath(List<String> packagePath);

    /**
     * 获取当前类过滤器的包路径
     *
     * @return
     */
    List<String> getPackagePath();

    /**
     * 是否有查找路径信息
     *
     * @return
     */
    boolean hasResource();
}
