package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;

public interface Model2BeanFactory {
    <T> T toJavaObject(ModelObject object, Class<T> tClass);

    <T> void toJavaObject(ModelObject object, T obj);
}
