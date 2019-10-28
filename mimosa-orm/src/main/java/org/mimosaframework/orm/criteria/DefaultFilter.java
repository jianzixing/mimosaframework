package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

/**
 * @author yangankang
 */
public class DefaultFilter implements Filter {

    private Query query;
    private Join join;
    private Update update;
    private Delete delete;
    private Function function;
    private Object key;
    private Object value;
    private Object startValue;
    private Object endValue;
    private String symbol;

    public DefaultFilter() {
    }

    public DefaultFilter(Query query) {
        this.query = query;
    }

    public DefaultFilter(Object key, Object value, String symbol) {
        this.key = key;
        this.value = value;
        if (symbol != null) {
            this.symbol = symbol.trim();
        }
    }

    public DefaultFilter(Join join) {
        this.join = join;
    }

    public DefaultFilter(Update update) {
        this.update = update;
    }

    public DefaultFilter(Delete delete) {
        this.delete = delete;
    }

    public DefaultFilter(Function function) {
        this.function = function;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public Object getStartValue() {
        return startValue;
    }

    public Object getEndValue() {
        return endValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setStartValue(Object startValue) {
        this.startValue = startValue;
    }

    public void setEndValue(Object endValue) {
        this.endValue = endValue;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Query query() {
        return this.query;
    }

    @Override
    public Join join() {
        return this.join;
    }

    @Override
    public Update update() {
        return this.update;
    }

    @Override
    public Delete delete() {
        return this.delete;
    }

    @Override
    public Filter eq(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = "=";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_not_allow_null"));
        }
        return this;
    }

    @Override
    public Filter in(Object key, Iterable values) {
        this.key = key;
        this.value = values;
        this.symbol = "in";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (values == null || !values.iterator().hasNext()) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_gt0"));
        }
        return this;
    }

    @Override
    public Filter in(Object key, Object... values) {
        this.key = key;
        this.value = values;
        this.symbol = "in";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_gt0"));
        }
        return this;
    }

    @Override
    public Filter nin(Object key, Iterable values) {
        this.key = key;
        this.value = values;
        this.symbol = "notIn";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (values == null || !values.iterator().hasNext()) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_gt0"));
        }
        return this;
    }

    @Override
    public Filter nin(Object key, Object... values) {
        this.key = key;
        this.value = values;
        this.symbol = "notIn";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_gt0"));
        }
        return this;
    }

    @Override
    public Filter like(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = "like";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_have"));
        }
        return this;
    }

    @Override
    public Filter ne(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = "!=";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_not_allow_null"));
        }
        return this;
    }

    @Override
    public Filter gt(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = ">";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_have"));
        }
        return this;
    }

    @Override
    public Filter gte(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = ">=";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_have"));
        }
        return this;
    }

    @Override
    public Filter lt(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = "<";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_have"));
        }
        return this;
    }

    @Override
    public Filter lte(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.symbol = "<=";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_must_have"));
        }
        return this;
    }

    @Override
    public Filter between(Object key, Object start, Object end) {
        this.key = key;
        this.startValue = start;
        this.endValue = end;
        this.symbol = "between";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "value_between_must"));
        }
        return this;
    }

    @Override
    public Filter isNull(Object key) {
        this.key = key;
        this.value = value;
        this.symbol = "isNull";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        return this;
    }

    @Override
    public Filter isNotNull(Object key) {
        this.key = key;
        this.value = value;
        this.symbol = "notNull";
        if (key == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultFilter.class, "key_not_allow_null"));
        }
        return this;
    }
}
