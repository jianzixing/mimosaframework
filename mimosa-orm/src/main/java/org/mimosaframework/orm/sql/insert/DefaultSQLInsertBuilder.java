package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLInsertBuilder
        implements
        RedefineInsertBuilder {

    @Override
    public Object insert() {
        return this;
    }


    @Override
    public Object columns(Serializable... fields) {
        return this;
    }

    @Override
    public Object table(Class table) {
        return this;
    }

    @Override
    public Object into() {
        return this;
    }

    @Override
    public Object values() {
        return this;
    }

    @Override
    public Object row(Object... values) {
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
