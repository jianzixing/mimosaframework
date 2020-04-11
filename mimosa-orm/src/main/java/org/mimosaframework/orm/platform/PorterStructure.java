package org.mimosaframework.orm.platform;

import java.util.List;
import java.util.Map;

public class PorterStructure {
    private TypeForRunner changerClassify;
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

    public PorterStructure(String sql, List<SQLDataPlaceholder> sqlDataPlaceholders) {
        this.sql = sql;
        this.sqlDataPlaceholders = sqlDataPlaceholders;
    }

    public PorterStructure(TypeForRunner changerClassify, SQLBuilder sqlBuilder) {
        this.changerClassify = changerClassify;
        this.sqlBuilder = sqlBuilder;
    }

    public PorterStructure(TypeForRunner changerClassify, SQLBuilder sqlBuilder, Map<Object, List<SelectFieldAliasReference>> references) {
        this.changerClassify = changerClassify;
        this.sqlBuilder = sqlBuilder;
        this.references = references;
    }

    public TypeForRunner getChangerClassify() {
        return changerClassify;
    }

    public void setChangerClassify(TypeForRunner changerClassify) {
        this.changerClassify = changerClassify;
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
