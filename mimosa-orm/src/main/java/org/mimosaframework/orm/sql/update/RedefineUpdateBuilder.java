package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;

public interface RedefineUpdateBuilder
        extends
        UpdateBuilder,
        AbsTableBuilder,
        SetBuilder,
        AbsWhereColumnBuilder,
        AbsWhereValueBuilder,
        OperatorBuilder,
        OperatorEqualBuilder,
        OperatorFunctionBuilder,
        BetweenValueBuilder,
        WhereBuilder,
        OrderByBuilder,
        SortBuilder,
        AbsValueBuilder,
        UpdateWhichTableBuilder,
        UpdateSetColumnBuilder {
}
