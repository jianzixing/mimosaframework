package org.mimosaframework.orm.sql.stamp;

import java.util.List;

public class StampSelect implements StampTables {
    public StampSelectField[] columns;
    public StampFrom[] froms;
    public StampWhere where;
    public StampColumn[] groupBy;
    public StampHaving having;
    public StampOrderBy[] orderBy;
    public StampLimit limit;

    @Override
    public List<STItem> getTables() {
        return null;
    }
}
