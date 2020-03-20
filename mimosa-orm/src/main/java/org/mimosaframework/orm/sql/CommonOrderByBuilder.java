package org.mimosaframework.orm.sql;

public interface CommonOrderByBuilder<T>
        extends
        OrderByBuilder<AbsWhereColumnBuilder<SortBuilder<CommonOrderByBuilder<T>>>>,
        LimitBuilder<T> {
}
