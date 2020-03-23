package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.*;

public interface RedefineCreateBuilder
        extends
        CreateBuilder,
        DatabaseBuilder,
        INEBuilder,
        AbsNameBuilder,
        CharsetBuilder,
        CollateBuilder,
        TableBuilder,
        CreateTableColumnBuilder,
        AbsExtraBuilder,
        UniqueBuilder,
        FullTextBuilder,
        IndexBuilder,
        OnBuilder,
        AbsTableBuilder,
        CreateIndexColumnsBuilder {
}
