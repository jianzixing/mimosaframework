package org.mimosaframework.orm.platform;

import java.util.List;

public class SQLBuilderCombine {
    private String sql;
    private List<SQLDataPlaceholder> placeholders;

    public SQLBuilderCombine(String sql, List<SQLDataPlaceholder> placeholders) {
        this.sql = sql;
        this.placeholders = placeholders;
    }

    public String getSql() {
        return sql;
    }

    public List<SQLDataPlaceholder> getPlaceholders() {
        return placeholders;
    }
}
