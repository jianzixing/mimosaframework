package org.mimosaframework.orm.platform;

import java.util.List;

public class JDBCTraversing {
    private TypeForRunner typeForRunner;
    private SQLBuilder sqlBuilder;
    private long primaryKeyValue;

    /**
     * 如果只执行sql语句
     */
    private String sql;
    /**
     * 预编译参数及值
     */
    private List<SQLDataPlaceholder> sqlDataPlaceholders;

    private boolean showSQL = true;

    public JDBCTraversing(String sql) {
        sql = sql.trim();
        this.sql = sql;
        this.setTypeFor(sql);
    }

    public JDBCTraversing(String sql, List<SQLDataPlaceholder> sqlDataPlaceholders) {
        sql = sql.trim();
        this.sql = sql;
        this.sqlDataPlaceholders = sqlDataPlaceholders;
        this.setTypeFor(sql);
    }

    public JDBCTraversing(String sql, List<SQLDataPlaceholder> sqlDataPlaceholders, boolean skipTypFor) {
        sql = sql.trim();
        this.sql = sql;
        this.sqlDataPlaceholders = sqlDataPlaceholders;
        if (!skipTypFor) this.setTypeFor(sql);
    }

    private void setTypeFor(String sql) {
        String[] strings = sql.split("\\s+|\\n");
        if (strings.length > 0) {
            if (strings[0].trim().equalsIgnoreCase("alter")) {
                this.typeForRunner = TypeForRunner.ALTER;
            } else if (strings[0].trim().equalsIgnoreCase("create")) {
                this.typeForRunner = TypeForRunner.CREATE;
            } else if (strings[0].trim().equalsIgnoreCase("delete")) {
                this.typeForRunner = TypeForRunner.DELETE;
            } else if (strings[0].trim().equalsIgnoreCase("drop")) {
                this.typeForRunner = TypeForRunner.DROP;
            } else if (strings[0].trim().equalsIgnoreCase("insert")) {
                this.typeForRunner = TypeForRunner.INSERT;
            } else if (strings[0].trim().equalsIgnoreCase("select")) {
                this.typeForRunner = TypeForRunner.SELECT;
            } else if (strings[0].trim().equalsIgnoreCase("update")) {
                this.typeForRunner = TypeForRunner.UPDATE;
            } else {
                this.typeForRunner = TypeForRunner.OTHER;
            }
        }
    }

    public JDBCTraversing(TypeForRunner typeForRunner, String sql, List<SQLDataPlaceholder> sqlDataPlaceholders) {
        this.typeForRunner = typeForRunner;
        this.sql = sql;
        this.sqlDataPlaceholders = sqlDataPlaceholders;
    }

    public JDBCTraversing(TypeForRunner typeForRunner, SQLBuilder sqlBuilder) {
        this.typeForRunner = typeForRunner;
        this.sqlBuilder = sqlBuilder;
    }

    public TypeForRunner getTypeForRunner() {
        return typeForRunner;
    }

    public void setTypeForRunner(TypeForRunner typeForRunner) {
        this.typeForRunner = typeForRunner;
    }

    public SQLBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public void setSqlBuilder(SQLBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public long getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(long primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<SQLDataPlaceholder> getSqlDataPlaceholders() {
        return sqlDataPlaceholders;
    }

    public void setSqlDataPlaceholders(List<SQLDataPlaceholder> sqlDataPlaceholders) {
        this.sqlDataPlaceholders = sqlDataPlaceholders;
    }

    public boolean isShowSQL() {
        return showSQL;
    }

    public void setShowSQL(boolean showSQL) {
        this.showSQL = showSQL;
    }
}
