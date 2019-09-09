package org.mimosaframework.orm.criteria;

import java.io.Serializable;

public class Value implements Serializable {
    private Object value;

    public static Value get(Object value) {
        return new Value(value);
    }

    public Value(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
