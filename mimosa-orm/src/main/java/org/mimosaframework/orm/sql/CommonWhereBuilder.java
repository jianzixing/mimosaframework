package org.mimosaframework.orm.sql;

public interface CommonWhereBuilder<T extends CommonWhereNextBuilder>
        extends
        AboutChildBuilder,
        WrapperBuilder<T>,
        OperatorLinkBuilder<CommonWhereCompareBuilder<T>, LogicBuilder<CommonWhereBuilder<T>>>,
        OperatorFunctionBuilder<T> {
}
