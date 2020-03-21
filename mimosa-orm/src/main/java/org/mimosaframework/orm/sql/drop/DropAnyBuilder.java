package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.*;

public interface DropAnyBuilder
        extends
        DatabaseBuilder<DropDatabaseBuilder<UnifyBuilder>>,
        TableBuilder<DropTableBuilder<UnifyBuilder>>,
        IndexBuilder<AbsNameBuilder<OnBuilder<AbsTableBuilder<UnifyBuilder>>>> {
}
