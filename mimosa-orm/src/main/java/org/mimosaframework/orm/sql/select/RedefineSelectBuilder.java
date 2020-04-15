package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface RedefineSelectBuilder
        extends
        SelectBuilder,
        SelectFieldBuilder,
        AsBuilder,
        FromBuilder,
        WhereBuilder,
        AbsWhereColumnBuilder,
        OperatorBuilder,
        OperatorFunctionBuilder,
        LogicBuilder,
        WrapperBuilder,
        HavingBuilder,
        FieldFunBuilder,
        AbsValueBuilder,
        OrderByBuilder,
        SortBuilder,
        LimitBuilder,
        AbsColumnBuilder,
        BetweenValueBuilder,
        AbsWhereValueBuilder,
        GroupByBuilder,

        LeftBuilder,
        InnerBuilder,
        JoinBuilder,
        OnBuilder,
        AbsTableBuilder,
        AbsTableAliasBuilder,
        SelectTableNameBuilder,

        CommonSymbolBuilder {
}
