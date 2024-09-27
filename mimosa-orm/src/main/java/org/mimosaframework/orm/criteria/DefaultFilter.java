package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.utils.SQLUtils;

/**
 * @author yangankang
 */
public class DefaultFilter extends AbstractFilter<DefaultFilter> implements QueryFilter<DefaultFilter> {
    private String as;
    private String key;
    private Object value;
    private Object startValue;
    private Object endValue;
    private String symbol;

    private Query query;

    public DefaultFilter() {
    }

    public DefaultFilter(Object key, Object value, String symbol) {
        this.key = ClassUtils.value(key);
        this.value = value;
        if (symbol != null) {
            this.symbol = symbol.trim();
        }
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getKey() {
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
        this.key = ClassUtils.value(key);
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

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public DefaultFilter as(Object as) {
        SQLUtils.checkAsName(ClassUtils.value(as));
        this.as = as.toString();
        return this;
    }

    @Override
    public DefaultFilter eq(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = "=";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_not_allow_null"));
        }
        return this;
    }

    @Override
    public DefaultFilter in(Object key, Iterable values) {
        this.key = ClassUtils.value(key);
        this.value = values;
        this.symbol = "in";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (values == null || !values.iterator().hasNext()) {
            throw new IllegalArgumentException(I18n.print("value_must_gt0"));
        }
        return this;
    }

    @Override
    public DefaultFilter in(Object key, Object... values) {
        this.key = ClassUtils.value(key);
        this.value = values;
        this.symbol = "in";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(I18n.print("value_must_gt0"));
        }
        return this;
    }

    @Override
    public DefaultFilter nin(Object key, Iterable values) {
        this.key = ClassUtils.value(key);
        this.value = values;
        this.symbol = "notIn";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (values == null || !values.iterator().hasNext()) {
            throw new IllegalArgumentException(I18n.print("value_must_gt0"));
        }
        return this;
    }

    @Override
    public DefaultFilter nin(Object key, Object... values) {
        this.key = ClassUtils.value(key);
        this.value = values;
        this.symbol = "notIn";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(I18n.print("value_must_gt0"));
        }
        return this;
    }

    @Override
    public DefaultFilter like(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = "like";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_must_have"));
        }
        return this;
    }

    @Override
    public DefaultFilter ne(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = "!=";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_not_allow_null"));
        }
        return this;
    }

    @Override
    public DefaultFilter gt(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = ">";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_must_have"));
        }
        return this;
    }

    @Override
    public DefaultFilter gte(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = ">=";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_must_have"));
        }
        return this;
    }

    @Override
    public DefaultFilter lt(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = "<";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_must_have"));
        }
        return this;
    }

    @Override
    public DefaultFilter lte(Object key, Object value) {
        this.key = ClassUtils.value(key);
        this.value = value;
        this.symbol = "<=";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (value == null) {
            throw new IllegalArgumentException(I18n.print("value_must_have"));
        }
        return this;
    }

    @Override
    public DefaultFilter between(Object key, Object start, Object end) {
        this.key = ClassUtils.value(key);
        this.startValue = start;
        this.endValue = end;
        this.symbol = "between";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException(I18n.print("value_between_must"));
        }
        return this;
    }

    @Override
    public DefaultFilter isNull(Object key) {
        this.key = ClassUtils.value(key);
        this.symbol = "isNull";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        return this;
    }

    @Override
    public DefaultFilter isNotNull(Object key) {
        this.key = ClassUtils.value(key);
        this.symbol = "notNull";
        if (key == null) {
            throw new IllegalArgumentException(I18n.print("key_not_allow_null"));
        }
        return this;
    }

    @Override
    public DefaultFilter exists(Query query) {
        this.symbol = "exists";
        this.query = query;
        return this;
    }
}
