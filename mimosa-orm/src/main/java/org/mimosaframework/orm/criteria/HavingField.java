package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

import java.io.Serializable;

public class HavingField implements Filter {
    private BasicFunction function;
    private Filter filter;
    private boolean distinct = false;

    public HavingField(BasicFunction function) {
        this.function = function;
    }

    public HavingField(BasicFunction function, boolean distinct) {
        this.function = function;
        this.distinct = distinct;
    }

    public BasicFunction getFunction() {
        return function;
    }

    public Filter getFilter() {
        return filter;
    }

    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public HavingField eq(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.eq(key, value);
        return this;
    }

    @Override
    public HavingField in(Serializable key, Iterable values) {
        this.filter = new DefaultFilter();
        this.filter.in(key, values);
        return this;
    }

    @Override
    public HavingField in(Serializable key, Object... values) {
        this.filter = new DefaultFilter();
        this.filter.in(key, values);
        return this;
    }

    @Override
    public HavingField nin(Serializable key, Iterable values) {
        this.filter = new DefaultFilter();
        this.filter.nin(key, values);
        return this;
    }

    @Override
    public HavingField nin(Serializable key, Object... values) {
        this.filter = new DefaultFilter();
        this.filter.nin(key, values);
        return this;
    }

    @Override
    public HavingField like(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.like(key, value);
        return this;
    }

    @Override
    public HavingField ne(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.ne(key, value);
        return this;
    }

    @Override
    public HavingField gt(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.gt(key, value);
        return this;
    }

    @Override
    public HavingField gte(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.gte(key, value);
        return this;
    }

    @Override
    public HavingField lt(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.lt(key, value);
        return this;
    }

    @Override
    public HavingField lte(Serializable key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.lte(key, value);
        return this;
    }

    @Override
    public HavingField between(Serializable key, Object start, Object end) {
        this.filter = new DefaultFilter();
        this.filter.between(key, start, end);
        return this;
    }

    @Override
    public HavingField isNull(Serializable key) {
        this.filter = new DefaultFilter();
        this.filter.isNull(key);
        return this;
    }

    @Override
    public HavingField isNotNull(Serializable key) {
        this.filter = new DefaultFilter();
        this.filter.isNotNull(key);
        return this;
    }
}
