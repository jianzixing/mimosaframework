package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface RedefineDeleteBuilder
        extends
        DeleteBuilder,
        AbsTableBuilder,
        AbsTablesBuilder,
        BeforeDeleteFromBuilder,
        FromBuilder,
        WhereBuilder,
        AbsWhereColumnBuilder,
        OperatorBuilder,
        AbsValueBuilder,
        BetweenValueBuilder,
        LogicBuilder,
        UsingBuilder,
        WrapperBuilder,
        OrderByBuilder,
        SortBuilder,
        LimitBuilder {
}
