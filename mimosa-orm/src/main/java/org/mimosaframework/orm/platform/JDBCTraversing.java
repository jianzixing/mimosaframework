package org.mimosaframework.orm.platform;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询时使用join后会产生各种别名字段
     */
    private Map<Object, List<SelectFieldAliasReference>> references;

    public JDBCTraversing(String sql) {
        this.sql = sql;
    }

    public JDBCTraversing(String sql, List<SQLDataPlaceholder> sqlDataPlaceholders) {
        this.sql = sql;
        this.sqlDataPlaceholders = sqlDataPlaceholders;
    }

    public JDBCTraversing(TypeForRunner typeForRunner, SQLBuilder sqlBuilder) {
        this.typeForRunner = typeForRunner;
        this.sqlBuilder = sqlBuilder;
    }

    public JDBCTraversing(TypeForRunner typeForRunner, SQLBuilder sqlBuilder, Map<Object, List<SelectFieldAliasReference>> references) {
        this.typeForRunner = typeForRunner;
        this.sqlBuilder = sqlBuilder;
        this.references = references;
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

    public Map<Object, List<SelectFieldAliasReference>> getReferences() {
        return references;
    }

    public void setReferences(Map<Object, List<SelectFieldAliasReference>> references) {
        this.references = references;
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
}
