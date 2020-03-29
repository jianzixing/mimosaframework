package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public class DefaultSQLDropBuilder
        extends AbstractSQLBuilder
        implements
        RedefineDropBuilder {

    @Override
    public Object drop() {
        return this;
    }

    @Override
    public Object name(Serializable value) {
        return this;
    }

    @Override
    public Object table(Class table) {
        return this;
    }

    @Override
    public Object database() {
        return this;
    }

    @Override
    public Object ifExist() {
        return this;
    }

    @Override
    public Object index() {
        return this;
    }

    @Override
    public Object on() {
        return this;
    }

    @Override
    public Object table() {
        return this;
    }
}
