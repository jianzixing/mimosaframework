package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;

public interface RedefineUpdateBuilder
        extends
        UpdateBuilder,
        AbsTablesBuilder,
        SetBuilder,
        AbsWhereColumnBuilder,
        OperatorEqualBuilder,
        SplitBuilder,
        WhereBuilder,
        OrderByBuilder,
        SortBuilder,
        LimitBuilder,
        AbsValueBuilder {
}
