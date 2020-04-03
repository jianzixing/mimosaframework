package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;

public interface RedefineUpdateBuilder
        extends
        UpdateBuilder,
        AbsTableAliasBuilder,
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
        LimitCountBuilder,
        AbsValueBuilder,
        UsingBuilder,
        UpdateWhichTableBuilder,
        UpdateSetColumnBuilder {
}
