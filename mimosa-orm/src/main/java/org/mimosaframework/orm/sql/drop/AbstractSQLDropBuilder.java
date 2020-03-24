package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLDropBuilder
        extends AbstractSQLBuilder
        implements
        RedefineDropBuilder {

    @Override
    public Object drop() {
        this.sqlBuilder.DROP();
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.sqlBuilder.addString(value.toString());
        return this;
    }

    @Override
    public Object table(Class table) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        return this;
    }

    @Override
    public Object database() {
        this.sqlBuilder.DATABASE();
        return this;
    }

    @Override
    public Object ifExist() {
        this.sqlBuilder.IF().EXISTS();
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
    public Object table() {
        this.sqlBuilder.TABLE();
        return this;
    }
}
