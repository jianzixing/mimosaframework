package org.mimosaframework.orm.sql;

public interface CommonWhereCompareBuilder<T>
        extends
        AbsWhereValueBuilder<T>,
        AbsColumnBuilder<T>,
        OperatorFunctionBuilder<T> {
}
