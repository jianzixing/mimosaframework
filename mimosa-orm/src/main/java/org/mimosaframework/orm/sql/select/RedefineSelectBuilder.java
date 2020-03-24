package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface RedefineSelectBuilder
        extends
        SelectBuilder,
        SelectFieldBuilder,
        FromBuilder,
        AbsTablesBuilder,
        WhereBuilder,
        AbsWhereColumnBuilder,
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
        SplitBuilder,

        LeftBuilder,
        InnerBuilder,
        JoinBuilder,
        OnBuilder,
        AbsTableBuilder {
}
