package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLInsertBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineInsertBuilder {

    @Override
    public Object insert() {
        this.gammars.add("insert");
        return this;
    }

    @Override
    public Object columns(Serializable... fields) {
        this.gammars.add("columns");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object into() {
        this.gammars.add("into");
        return this;
    }

    @Override
    public Object values() {
        this.gammars.add("values");
        return this;
    }

    @Override
    public Object row(Object... values) {
        this.gammars.add("row");
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
