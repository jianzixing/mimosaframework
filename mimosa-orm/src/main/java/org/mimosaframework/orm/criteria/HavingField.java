package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

public class HavingField extends AbstractFilter<HavingField> implements Filter<HavingField> {
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
    public HavingField eq(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.eq(key, value);
        return this;
    }

    @Override
    public HavingField in(Object key, Iterable values) {
        this.filter = new DefaultFilter();
        this.filter.in(key, values);
        return this;
    }

    @Override
    public HavingField in(Object key, Object... values) {
        this.filter = new DefaultFilter();
        this.filter.in(key, values);
        return this;
    }

    @Override
    public HavingField nin(Object key, Iterable values) {
        this.filter = new DefaultFilter();
        this.filter.nin(key, values);
        return this;
    }

    @Override
    public HavingField nin(Object key, Object... values) {
        this.filter = new DefaultFilter();
        this.filter.nin(key, values);
        return this;
    }

    @Override
    public HavingField like(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.like(key, value);
        return this;
    }

    @Override
    public HavingField ne(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.ne(key, value);
        return this;
    }

    @Override
    public HavingField gt(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.gt(key, value);
        return this;
    }

    @Override
    public HavingField gte(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.gte(key, value);
        return this;
    }

    @Override
    public HavingField lt(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.lt(key, value);
        return this;
    }

    @Override
    public HavingField lte(Object key, Object value) {
        this.filter = new DefaultFilter();
        this.filter.lte(key, value);
        return this;
    }

    @Override
    public HavingField between(Object key, Object start, Object end) {
        this.filter = new DefaultFilter();
        this.filter.between(key, start, end);
        return this;
    }

    @Override
    public HavingField isNull(Object key) {
        this.filter = new DefaultFilter();
        this.filter.isNull(key);
        return this;
    }

    @Override
    public HavingField isNotNull(Object key) {
        this.filter = new DefaultFilter();
        this.filter.isNotNull(key);
        return this;
    }
}
