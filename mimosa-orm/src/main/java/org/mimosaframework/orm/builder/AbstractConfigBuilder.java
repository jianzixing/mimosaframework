package org.mimosaframework.orm.builder;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import javax.sql.DataSource;
import java.util.*;

public abstract class AbstractConfigBuilder implements ConfigBuilder {

    protected Set<Class> getMappingClass() throws ContextException {
        String mappingClassPackage = this.getMappingClassPackage();
        Set<String> additionClasses = this.getAdditionMappingClass();
        return BuilderUtils.getMappingClass(mappingClassPackage, additionClasses);
    }

    protected NamingConvert getConvert(String convertClass, String convertName, Map properties) throws ContextException {
        return BuilderUtils.getConvert(convertClass, convertName, properties);
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
                throw new ContextException(Messages.get(LanguageMessageFactory.PROJECT, AbstractConfigBuilder.class, "data_source_fail"), e);
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
