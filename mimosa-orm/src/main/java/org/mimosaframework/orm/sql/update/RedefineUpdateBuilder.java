package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;

public interface RedefineUpdateBuilder
        extends
        UpdateBuilder,
        AbsTablesBuilder,
        SetBuilder,
        AbsWhereColumnBuilder,
        AbsWhereValueBuilder,
        OperatorEqualBuilder,
        OperatorFunctionBuilder,
        BetweenValueBuilder,
        SplitBuilder,
        WhereBuilder,
        OrderByBuilder,
        SortBuilder,
        LimitBuilder,
        AbsValueBuilder {
}
