package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampDelete implements StampAction {
    public Class delTable;
    public String delTableAlias;

    public StampFrom[] froms;

    public StampFrom[] using;

    public StampWhere where;

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
        if (using != null) {
            for (StampFrom ui : using) {
                if (ui.table != null) {
                    items.add(new STItem(ui.table, ui.aliasName));
                }
            }
        }
        return items;
    }
}
