package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.utils.ClassUtils;

public class CriteriaUtils {
    public static Object getFilterKey(Object key) {
        if (!(key instanceof AsField)) {
            key = ClassUtils.value(key);
        }
        return key;
    }
}
