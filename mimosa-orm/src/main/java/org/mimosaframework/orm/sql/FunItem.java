package org.mimosaframework.orm.sql;

import java.io.Serializable;

public class FunItem implements FieldFunBuilder {
    private String funName;
    private Serializable[] params;

    public String getFunName() {
        return funName;
    }

    public Serializable[] getParams() {
        return params;
    }

    public Object fun(String funName, Serializable... params) {
        this.funName = funName;
        this.params = params;
        return this;
    }

    @Override
    public Object count(Serializable... params) {
        this.params = params;
        this.funName = Support.COUNT.toString();
        return this;
    }

    @Override
    public Object max(Serializable... params) {
        this.params = params;
        this.funName = Support.MAX.toString();
        return this;
    }

    @Override
    public Object avg(Serializable... params) {
        this.params = params;
        this.funName = Support.AVG.toString();
        return this;
    }

    @Override
    public Object sum(Serializable... params) {
        this.params = params;
        this.funName = Support.SUM.toString();
        return this;
    }

    @Override
    public Object min(Serializable... params) {
        this.params = params;
        this.funName = Support.MIN.toString();
        return this;
    }

    @Override
    public Object concat(Serializable... params) {
        this.params = params;
        this.funName = Support.CONCAT.toString();
        return this;
    }

    @Override
    public Object substring(Serializable param, int pos, int len) {
        this.params = new Serializable[]{param, pos, len};
        this.funName = Support.SUBSTRING.toString();
        return this;
    }
}
