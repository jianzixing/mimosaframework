package org.mimosaframework.orm.sql.stamp;

import java.util.List;

public class StampStructure implements StampAction {
    public int type; // 0 tables  1 columns
    public String schema;
    public List<String> tables;

    @Override
    public List<STItem> getTables() {
        return null;
    }
}
