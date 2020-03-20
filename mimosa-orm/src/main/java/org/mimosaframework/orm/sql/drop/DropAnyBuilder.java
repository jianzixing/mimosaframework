package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.*;

public interface DropAnyBuilder
        extends
        DatabaseBuilder<DropDatabaseBuilder>,
        TableBuilder<DropTableBuilder>,
        IndexBuilder<AbsNameBuilder<OnBuilder<AbsTableBuilder>>> {
}
