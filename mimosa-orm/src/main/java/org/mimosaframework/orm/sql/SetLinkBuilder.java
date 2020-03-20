package org.mimosaframework.orm.sql;

/**
 * for update set SetLinkBuilder
 *
 * @param <T>
 */
public interface SetLinkBuilder<T>
        extends
        AbsWhereColumnBuilder<OperatorEqualBuilder<AbsWhereValueBuilder<T>>> {
}
