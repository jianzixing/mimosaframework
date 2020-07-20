package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampSelect implements StampAction {
    public StampSelectField[] columns;
    public StampFrom[] froms;
    public StampSelectJoin[] joins;
    public StampWhere where;
    public StampColumn[] groupBy;
    public StampWhere having;
    public StampOrderBy[] orderBy;
    public StampLimit limit;
    public boolean forUpdate;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = null;
        if (froms != null) {
            for (StampFrom from : froms) {
                if (from.table != null) {
                    if (items == null) items = new ArrayList<>();
                    items.add(new STItem(from.table, from.aliasName));
                }
            }
        }
        if (joins != null) {
            for (StampSelectJoin join : joins) {
                if (join.tableClass != null) {
                    if (items == null) items = new ArrayList<>();
                    items.add(new STItem(join.tableClass, join.tableAliasName));
                }
            }
        }
        return items;
    }

    @Override
    public StampSelect clone() {
        StampSelect stampSelect = new StampSelect();
        stampSelect.columns = this.columns;
        stampSelect.froms = this.froms;
        stampSelect.joins = this.joins;
        stampSelect.where = this.where;
        stampSelect.groupBy = this.groupBy;
        stampSelect.having = this.having;
        stampSelect.orderBy = this.orderBy;
        stampSelect.limit = this.limit;
        stampSelect.forUpdate = this.forUpdate;
        return stampSelect;
    }
}
