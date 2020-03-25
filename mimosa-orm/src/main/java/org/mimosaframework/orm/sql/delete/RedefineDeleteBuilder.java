package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface RedefineDeleteBuilder
        extends
        DeleteBuilder,
        AbsTableBuilder,
        AbsTableAliasBuilder,
        BeforeDeleteFromBuilder,
        FromBuilder,
        WhereBuilder,
        AbsWhereColumnBuilder,
        AbsWhereValueBuilder,
        OperatorBuilder,
        OperatorFunctionBuilder,
        AbsValueBuilder,
        BetweenValueBuilder,
        LogicBuilder,
        UsingBuilder,
        WrapperBuilder,
        OrderByBuilder,
        SortBuilder,
        LimitBuilder {
}
