package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.KeyTarget;
import org.mimosaframework.orm.sql.stamp.StampDrop;

import java.io.Serializable;

public class DefaultSQLDropBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineDropBuilder {

    protected StampDrop stampDrop = new StampDrop();

    @Override
    public DefaultSQLDropBuilder drop() {
        this.gammars.add("drop");
        return this;
    }

    @Override
    public DefaultSQLDropBuilder name(Serializable value) {
        this.gammars.add("name");
        if (this.point.equals("database")) {
            this.stampDrop.databaseName = value.toString();
        }
        if (this.point.equals("index")) {
            this.stampDrop.indexName = value.toString();
        }
        if (this.point.equals("table")) {
            this.stampDrop.tableName = value.toString();
        }
        return this;
    }

    @Override
    public DefaultSQLDropBuilder table(Class table) {
        this.gammars.add("table");
        this.stampDrop.tableClass = table;
        return this;
    }

    @Override
    public DefaultSQLDropBuilder database() {
        this.addPoint("database");
        stampDrop.target = KeyTarget.DATABASE;
        return this;
    }

    @Override
    public DefaultSQLDropBuilder ifExist() {
        this.gammars.add("ifExist");
        stampDrop.checkExist = true;
        return this;
    }

    @Override
    public DefaultSQLDropBuilder index() {
        this.addPoint("index");
        stampDrop.target = KeyTarget.INDEX;
        return this;
    }

    @Override
    public DefaultSQLDropBuilder on() {
        this.gammars.add("on");
        return this;
    }

    @Override
    public DefaultSQLDropBuilder table() {
        this.addPoint("table");
        stampDrop.target = KeyTarget.TABLE;
        return this;
    }

    @Override
    public StampDrop compile() {
        return this.stampDrop;
    }

    @Override
    public DefaultSQLDropBuilder table(String name) {
        this.gammars.add("table");
        this.stampDrop.tableName = name;
        return this;
    }
}
