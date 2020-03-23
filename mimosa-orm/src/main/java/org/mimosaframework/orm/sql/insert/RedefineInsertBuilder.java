package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.*;

public interface RedefineInsertBuilder
        extends
        InsertBuilder,
        IntoBuilder,
        AbsTableBuilder,
        AbsColumnsBuilder,
        ValuesBuilder,
        InsertValuesBuilder,
        SplitBuilder {
}
