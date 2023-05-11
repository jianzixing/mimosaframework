package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.criteria.DefaultQuery;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PlatformExecutorSelectContext {
    private DefaultQuery query;
    private ModelObjectConvertKey convert;
    private PlatformDialect dialect;
    private Map<Object, List<SelectFieldAliasReference>> fieldAlias;
    private AtomicInteger counter;

    public DefaultQuery getQuery() {
        return query;
    }

    public void setQuery(DefaultQuery query) {
        this.query = query;
    }

    public ModelObjectConvertKey getConvert() {
        return convert;
    }

    public void setConvert(ModelObjectConvertKey convert) {
        this.convert = convert;
    }

    public PlatformDialect getDialect() {
        return dialect;
    }

    public void setDialect(PlatformDialect dialect) {
        this.dialect = dialect;
    }

    public Map<Object, List<SelectFieldAliasReference>> getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(Map<Object, List<SelectFieldAliasReference>> fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public void setCounter(AtomicInteger counter) {
        this.counter = counter;
    }

    public PlatformExecutorSelectContext clone() {
        PlatformExecutorSelectContext context = new PlatformExecutorSelectContext();
        context.query = query;
        context.convert = convert;
        context.dialect = dialect;
        context.fieldAlias = fieldAlias;
        context.counter = counter;
        return context;
    }
}
