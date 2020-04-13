package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.*;

public interface RedefineInsertBuilder
        extends
        InsertBuilder,
        IntoBuilder,
        InsertTableNameBuilder,
        AbsColumnsBuilder,
        ValuesBuilder,
        InsertValuesBuilder {
}
