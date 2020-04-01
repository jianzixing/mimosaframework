package org.mimosaframework.orm.sql;

public interface OperatorLogicFunBuilder<T>
        extends
        LogicBuilder<T>,
        OperatorFunctionBuilder<OperatorLogicFunBuilder<T>> {
}
