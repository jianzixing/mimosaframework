package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.i18n.I18n;

public class ModelObjectToBean {
    public static <T> T toJavaObject(ModelObject object, Class<T> tClass) {

    }

    public static <T> void toJavaObject(ModelObject object, T obj) {
        try {

        } catch (Exception e) {
            throw new RuntimeException(I18n.print("model_to_bean_error"), e);
        }
    }
}
