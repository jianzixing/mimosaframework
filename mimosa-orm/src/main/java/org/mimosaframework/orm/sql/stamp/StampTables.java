package org.mimosaframework.orm.sql.stamp;

import java.util.List;

public interface StampTables {
    List<STItem> getTables();

    class STItem {
        public Class table;
        public Class tableAliasName;
    }
}
