package org.mimosaframework.orm.builder;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.DefaultFilterPackageClass;
import org.mimosaframework.core.utils.FilterPackageClass;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.convert.ConvertFactory;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.exception.ContextException;

import javax.sql.DataSource;
import java.util.*;

public abstract class AbstractConfigBuilder implements ConfigBuilder {

    protected Set<Class> getMappingClass() throws ContextException {
        String mappingClassPackage = this.getMappingClassPackage();
        Set<String> additionClasses = this.getAdditionMappingClass();

        BasicSetting basicDisposition = this.getBasicInfo();
        if (basicDisposition == null) {
            throw new ContextException("需要先初始化BasicDisposition拿到Convert实例");
        }

        FilterPackageClass filterPackageClass = new DefaultFilterPackageClass();
        filterPackageClass.setPackagePath(Arrays.asList(new String[]{mappingClassPackage}));
        Set<Class> classes = filterPackageClass.getScanClass(Table.class);
        if (classes == null && additionClasses != null && additionClasses.size() > 0) {
            classes = new LinkedHashSet<>();
        }
        if (classes != null) {
            Set<String> self = new LinkedHashSet();
            if (additionClasses != null) {
                for (String s : additionClasses) {
                    for (Class<?> clazz : classes) {
                        if (clazz.getName().equals(s) || clazz.getSimpleName().equals(s)) {
                            self.add(s);
                        }
                    }
                }
                additionClasses.removeAll(self);
                for (String s : additionClasses) {
                    try {
                        classes.add(Class.forName(s));
                    } catch (ClassNotFoundException e) {
                        throw new ContextException("没有找到映射类 " + s, e);
                    }
                }
            }
            return classes;
        }
        return null;
    }

    protected MappingNamedConvert getConvert(String convertClass, String convertName) throws ContextException {
        if (convertClass != null) convertClass = convertClass.trim();
        if (convertName != null) convertName = convertName.trim();
        // 类名和数据库字段名称的转换
        if (StringTools.isNotEmpty(convertClass)) {
            try {
                Class<? extends MappingNamedConvert> c = (Class<? extends MappingNamedConvert>) Class.forName(convertClass);
                return c.newInstance();
            } catch (Exception e) {
                throw new ContextException("实例化 ConvertClass 出错", e);
            }
        }
        if (StringTools.isEmpty(convertClass) && StringTools.isNotEmpty(convertName)) {
            MappingNamedConvert convert = ConvertFactory.getConvert(convertName);
            if (convert == null) {
                throw new ContextException("字段名称转换器 " + convertName + " 不存在");
            }
            return convert;
        }
        return null;
    }

    protected boolean isStringTrue(String s) {
        return (s != null && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equals("1")) ? true : false);
    }

    protected DataSource getDataSourceFromProperties(Map map) throws ContextException {
        String clazz = (String) map.get("dataSourceClass");
        map.remove("dataSourceClass");
        Class<? extends DataSource> dsClass = null;
        if (StringTools.isNotEmpty(clazz)) {
            try {
                dsClass = (Class<? extends DataSource>) Class.forName(clazz);
            } catch (Exception e) {
                throw new ContextException("加载DataSourceClass类失败", e);
            }
            DataSource ds = ModelObject.toJavaObject(new ModelObject(map), dsClass);
            return ds;
        }
        return null;
    }

    /**
     * 获得程序扫描的包名称
     * 使用这个包下所有@Table注解的类进行Mapping映射
     *
     * @return
     */
    protected abstract String getMappingClassPackage();

    /**
     * 获得附加的类名称
     * 使用这个类进行Mapping映射,即使没有用@Table注解
     *
     * @return
     */
    protected abstract Set<String> getAdditionMappingClass();
}
