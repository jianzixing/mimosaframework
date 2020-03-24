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
        AbsWhereValueBuilder,

        LeftBuilder,
        InnerBuilder,
        JoinBuilder,
        OnBuilder,
        AbsTableBuilder {
}
