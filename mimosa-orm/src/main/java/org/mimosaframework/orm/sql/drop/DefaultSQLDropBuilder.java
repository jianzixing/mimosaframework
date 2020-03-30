package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLDropBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineDropBuilder {

    @Override
    public Object drop() {
        this.gammars.add("drop");
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.gammars.add("name");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object database() {
        this.gammars.add("database");
        return this;
    }

    @Override
    public Object ifExist() {
        this.gammars.add("ifExist");
        return this;
    }

    @Override
    public Object index() {
        this.gammars.add("index");
        return this;
    }

    @Override
    public Object on() {
        this.gammars.add("on");
        return this;
    }

    @Override
    public Object table() {
        this.gammars.add("table");
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
