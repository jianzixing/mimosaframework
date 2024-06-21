package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.utils.SQLUtils;

import java.io.Serializable;

public class AsField implements Serializable {
    private String alias;
    private String field;

    public AsField(String alias, Object field) {
        SQLUtils.checkAsName(alias);
        this.alias = alias;
        this.field = ClassUtils.value(field);
    }

    public String getAlias() {
        return alias;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        return alias + "." + field;
    }
}
