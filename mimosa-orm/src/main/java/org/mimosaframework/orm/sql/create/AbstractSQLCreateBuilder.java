package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLCreateBuilder
        extends
        AbstractSQLBuilder

        implements
        RedefineCreateBuilder {

    @Override
    public Object create() {
        this.sqlBuilder.CREATE();
        return this;
    }

    @Override
    public Object database() {
        this.sqlBuilder.DATABASE();
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.sqlBuilder.addString(value.toString());
        return this;
    }

    @Override
    public Object charset(String charset) {
        this.sqlBuilder.CHARSET().addString(charset);
        return this;
    }

    @Override
    public Object collate(String collate) {
        this.sqlBuilder.COLLATE().addString(collate);
        return this;
    }

    @Override
    public Object ifNotExist() {
        this.sqlBuilder.IF().NOT().EXISTS();
        return this;
    }

    @Override
    public Object extra(String sql) {
        this.sqlBuilder.addString(sql);
        return this;
    }

    @Override
    public Object table() {
        this.sqlBuilder.TABLE();
        return this;
    }

    @Override
    public Object columns(AboutChildBuilder... columns) {
        if (columns != null) {
            int i = 0;
            for (AboutChildBuilder column : columns) {
                SQLBuilder sqlBuilder = column.getSqlBuilder();
                i++;
                sqlBuilder.setTableFieldReplaceRule(this.sqlBuilder.getRuleStart(), this.sqlBuilder.getRuleFinish());
                this.sqlBuilder.addSQLBuilder(sqlBuilder);
                if (i != columns.length) {
                    sqlBuilder.addSplit();
                }
            }
        }
        return this;
    }

    @Override
    public Object table(Class table) {
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addString(mappingTable.getMappingTableName());
        return this;
    }

    @Override
    public Object fullText() {
        this.sqlBuilder.FULLTEXT();
        return this;
    }

    @Override
    public Object index() {
        this.sqlBuilder.INDEX();
        return this;
    }

    @Override
    public Object on() {
        this.sqlBuilder.ON();
        return this;
    }

    @Override
    public Object unique() {
        this.sqlBuilder.UNIQUE();
        return this;
    }

    @Override
    public Object columns(Serializable... columns) {
        if (columns != null) {
            int i = 0;
            for (Serializable column : columns) {
                this.sqlBuilder.addWrapString(column.toString());
                i++;
                if (i != columns.length) this.sqlBuilder.addSplit();
            }
        }
        return this;
    }
}
