package org.mimosaframework.orm.sql.stamp;

import java.util.List;

public interface StampAction {
    List<STItem> getTables();

    class STItem {
        private Class table;
        private String tableAliasName;

        public STItem(Class table, String tableAliasName) {
            this.table = table;
            this.tableAliasName = tableAliasName;
        }

        public STItem(Class table) {
            this.table = table;
        }

        public Class getTable() {
            return table;
        }

        public void setTable(Class table) {
            this.table = table;
        }

        public String getTableAliasName() {
            return tableAliasName;
        }

        public void setTableAliasName(String tableAliasName) {
            this.tableAliasName = tableAliasName;
        }
    }
}
