package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface RedefineDeleteBuilder
        extends
        DeleteBuilder,
        AbsTableBuilder,
        AbsTableAliasBuilder,
        DeleteWhichBuilder,
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
        SortBuilder {
}
