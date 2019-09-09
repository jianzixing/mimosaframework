package org.mimosaframework.orm.scripting;

import org.mimosaframework.orm.platform.SQLDataPlaceholder;

import java.util.List;

public class BoundSql {
    private String sql;
    private String action;
    private List<SQLDataPlaceholder> dataPlaceholders;

    public BoundSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<SQLDataPlaceholder> getDataPlaceholders() {
        return dataPlaceholders;
    }

    public void setDataPlaceholders(List<SQLDataPlaceholder> dataPlaceholders) {
        this.dataPlaceholders = dataPlaceholders;
    }
}
