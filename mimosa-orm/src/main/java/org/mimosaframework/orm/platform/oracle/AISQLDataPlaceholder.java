package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.platform.SQLDataPlaceholder;

/**
 * 改类表示替换当前的值是查询的SQL值
 */
public class AISQLDataPlaceholder extends SQLDataPlaceholder {
    private String sql;

    public AISQLDataPlaceholder() {
    }

    public AISQLDataPlaceholder(String name, Object value, String sql) {
        super(name, value);
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
