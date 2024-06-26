package org.mimosaframework.orm.criteria;


import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.BasicFunction;

import java.io.Serializable;

public class FunctionField implements Serializable {
    private String field;
    private BasicFunction function;
    private String alias;
    // 处理精度
    private int scale = 5;
    private boolean distinct = false;
    private String avgCountName;

    public FunctionField(Object field, BasicFunction function) {
        this.field = ClassUtils.value(field);
        this.function = function;
    }

    public FunctionField(Object field, BasicFunction function, String alias) {
        this.field = ClassUtils.value(field);
        this.function = function;
        this.alias = alias;
    }

    public FunctionField(Object field, BasicFunction function, int scale) {
        this.field = ClassUtils.value(field);
        this.function = function;
        this.scale = scale;
    }

    public FunctionField(Object field, BasicFunction function, String alias, int scale) {
        this.field = ClassUtils.value(field);
        this.function = function;
        this.alias = alias;
        this.scale = scale;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = ClassUtils.value(field);
    }

    public BasicFunction getFunction() {
        return function;
    }

    public void setFunction(BasicFunction function) {
        this.function = function;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getAvgCountName() {
        return avgCountName;
    }

    public void setAvgCountName(String avgCountName) {
        this.avgCountName = avgCountName;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}
