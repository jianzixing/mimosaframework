package org.mimosaframework.orm.criteria;


import org.mimosaframework.orm.BasicFunction;

import java.io.Serializable;

public class FunctionField implements Serializable {
    private Object field;
    private BasicFunction function;
    private String alias;
    // 处理精度
    private int scale = 5;
    private String avgCountName;

    public FunctionField(Object field, BasicFunction function) {
        this.field = field;
        this.function = function;
    }

    public FunctionField(Object field, BasicFunction function, String alias) {
        this.field = field;
        this.function = function;
        this.alias = alias;
    }

    public FunctionField(Object field, BasicFunction function, int scale) {
        this.field = field;
        this.function = function;
        this.scale = scale;
    }

    public FunctionField(Object field, BasicFunction function, String alias, int scale) {
        this.field = field;
        this.function = function;
        this.alias = alias;
        this.scale = scale;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
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
}
