package org.mimosaframework.orm.criteria;

import java.io.Serializable;

public class AsField {
    private String alias;
    private String field;

    public AsField(String alias, Serializable field) {
        this.alias = alias;
        this.field = field.toString();
    }

    public String getAlias() {
        return alias;
    }

    public String getField() {
        return field;
    }
}
