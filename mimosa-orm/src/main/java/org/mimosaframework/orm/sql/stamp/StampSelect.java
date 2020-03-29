package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampSelect implements StampTables {
    public StampSelectField[] columns;
    public StampFrom[] froms;
    public StampSelectJoin[] joins;
    public StampWhere where;
    public StampColumn[] groupBy;
    public StampHaving having;
    public StampOrderBy[] orderBy;
    public StampLimit limit;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (froms != null) {
            for (StampFrom from : froms) {
                if (from.table != null) {
                    items.add(new STItem(from.table, from.aliasName));
                }
            }
        }
        if (joins != null) {
            for (StampSelectJoin join : joins) {
                if (join.table != null) {
                    items.add(new STItem(join.table, join.tableAliasName));
                }
            }
        }
        return items;
    }
}
