package org.mimosaframework.orm.sql.test.drop;

import org.mimosaframework.orm.sql.test.*;

public interface DropAnyBuilder
        extends
        DatabaseBuilder<DropDatabaseBuilder>,
        TableBuilder<DropTableBuilder>,
        IndexBuilder<AbsNameBuilder<OnBuilder<AbsTableBuilder>>> {
}
