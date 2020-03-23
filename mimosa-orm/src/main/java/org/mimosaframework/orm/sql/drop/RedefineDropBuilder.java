package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.*;

public interface RedefineDropBuilder
        extends
        UnifyBuilder,

        DropBuilder,
        DatabaseBuilder,
        IEBuilder,
        AbsNameBuilder,
        TableBuilder,
        AbsTableBuilder,
        IndexBuilder,
        OnBuilder {
}
