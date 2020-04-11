package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.KeyTarget;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampDrop;

import java.io.Serializable;

public class DefaultSQLDropBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineDropBuilder {

    protected StampDrop stampDrop = new StampDrop();

    @Override
    public Object drop() {
        this.gammars.add("drop");
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.gammars.add("name");
        this.stampDrop.name = value.toString();
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        this.stampDrop.table = table;
        return this;
    }

    @Override
    public Object database() {
        this.gammars.add("database");
        stampDrop.target = KeyTarget.DATABASE;
        return this;
    }

    @Override
    public Object ifExist() {
        this.gammars.add("ifExist");
        stampDrop.checkExist = true;
        return this;
    }

    @Override
    public Object index() {
        this.gammars.add("index");
        stampDrop.target = KeyTarget.INDEX;
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
        stampDrop.target = KeyTarget.TABLE;
        return this;
    }

    @Override
    public StampAction compile() {
        return this.stampDrop;
    }

    @Override
    public Object table(String name) {

        return this;
    }
}
