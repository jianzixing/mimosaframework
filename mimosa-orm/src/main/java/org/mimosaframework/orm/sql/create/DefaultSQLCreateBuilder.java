package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLCreateBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineCreateBuilder {

    @Override
    public Object create() {
        this.gammars.add("create");
        return this;
    }

    @Override
    public Object database() {
        this.gammars.add("database");
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.gammars.add("name");
        return this;
    }

    @Override
    public Object name(Class table) {
        this.gammars.add("name");
        return this;
    }

    @Override
    public Object charset(String charset) {
        this.gammars.add("charset");
        return this;
    }

    @Override
    public Object collate(String collate) {
        this.gammars.add("collate");
        return this;
    }

    @Override
    public Object ifNotExist() {
        this.gammars.add("ifNotExist");
        return this;
    }

    @Override
    public Object extra(String sql) {
        this.gammars.add("extra");
        return this;
    }

    @Override
    public Object table() {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object fullText() {
        this.gammars.add("fullText");
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
    public Object unique() {
        this.gammars.add("unique");
        return this;
    }

    @Override
    public Object columns(Serializable... columns) {
        this.gammars.add("columns");
        return this;
    }


    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        return this;
    }

    @Override
    public Object autoIncrement() {
        this.gammars.add("autoIncrement");
        return this;
    }

    @Override
    public Object comment(String comment) {
        this.gammars.add("comment");
        return this;
    }

    @Override
    public Object defaultValue(String value) {
        this.gammars.add("defaultValue");
        return this;
    }

    @Override
    public Object key() {
        this.gammars.add("key");
        return this;
    }

    @Override
    public Object not() {
        this.gammars.add("not");
        return this;
    }

    @Override
    public Object nullable() {
        this.gammars.add("nullable");
        return this;
    }

    @Override
    public Object primary() {
        this.gammars.add("primary");
        return this;
    }

    @Override
    public Object intType() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object varchar(int len) {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object charType(int len) {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object blob() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object text() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object tinyint() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object smallint() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object mediumint() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object bit() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object bigint() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object floatType() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object doubleType() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object decimal(int len, int scale) {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object booleanType() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object date() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object time() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object datetime() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object timestamp() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public Object year() {
        this.gammars.add("type");
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
