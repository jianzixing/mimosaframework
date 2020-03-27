package org.mimosaframework.orm.sql;

import java.io.Serializable;

public class FunItem implements FieldFunBuilder {
    private Serializable param;
    private Serializable[] params;
    private int pos;
    private int len;

    private Support type;

    @Override
    public Object count(Serializable... params) {
        this.params = params;
        this.type = Support.COUNT;
        return this;
    }

    @Override
    public Object max(Serializable... params) {
        this.params = params;
        this.type = Support.MAX;
        return this;
    }

    @Override
    public Object avg(Serializable... params) {
        this.params = params;
        this.type = Support.AVG;
        return this;
    }

    @Override
    public Object sum(Serializable... params) {
        this.params = params;
        this.type = Support.SUM;
        return this;
    }

    @Override
    public Object min(Serializable... params) {
        this.params = params;
        this.type = Support.MIN;
        return this;
    }

    @Override
    public Object concat(Serializable... params) {
        this.params = params;
        this.type = Support.CONCAT;
        return this;
    }

    @Override
    public Object substring(Serializable param, int pos, int len) {
        this.param = param;
        this.pos = pos;
        this.len = len;
        this.type = Support.SUBSTRING;
        return this;
    }

    public Serializable getParam() {
        return param;
    }

    public Serializable[] getParams() {
        return params;
    }

    public int getPos() {
        return pos;
    }

    public int getLen() {
        return len;
    }

    public Support getType() {
        return type;
    }
}
