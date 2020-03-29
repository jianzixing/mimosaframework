package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLCreateBuilder

        implements
        RedefineCreateBuilder {

    @Override
    public Object create() {
        return this;
    }

    @Override
    public Object database() {
        return this;
    }

    @Override
    public Object name(Serializable value) {
        return this;
    }

    @Override
    public Object name(Class table) {
        return this;
    }

    @Override
    public Object charset(String charset) {
        return this;
    }

    @Override
    public Object collate(String collate) {
        return this;
    }

    @Override
    public Object ifNotExist() {
        return this;
    }

    @Override
    public Object extra(String sql) {
        return this;
    }

    @Override
    public Object table() {
        return this;
    }

    @Override
    public Object table(Class table) {
        return this;
    }

    @Override
    public Object fullText() {
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
    public Object unique() {
        return this;
    }

    @Override
    public Object columns(Serializable... columns) {
        return this;
    }


    @Override
    public Object column(Serializable field) {

        return this;
    }

    @Override
    public Object autoIncrement() {
        return this;
    }

    @Override
    public Object comment(String comment) {
        return this;
    }

    @Override
    public Object defaults() {
        return this;
    }

    @Override
    public Object key() {
        return this;
    }

    @Override
    public Object not() {
        return this;
    }

    @Override
    public Object nullable() {
        return this;
    }

    @Override
    public Object primary() {
        return this;
    }

    @Override
    public Object intType() {
        return this;
    }

    @Override
    public Object varchar(int len) {
        return this;
    }

    @Override
    public Object charType(int len) {
        return this;
    }

    @Override
    public Object blob() {
        return this;
    }

    @Override
    public Object text() {
        return this;
    }

    @Override
    public Object tinyint() {
        return this;
    }

    @Override
    public Object smallint() {
        return this;
    }

    @Override
    public Object mediumint() {
        return this;
    }

    @Override
    public Object bit() {
        return this;
    }

    @Override
    public Object bigint() {
        return this;
    }

    @Override
    public Object floatType() {
        return this;
    }

    @Override
    public Object doubleType() {
        return this;
    }

    @Override
    public Object decimal(int len, int scale) {
        return this;
    }

    @Override
    public Object booleanType() {
        return this;
    }

    @Override
    public Object date() {
        return this;
    }

    @Override
    public Object time() {
        return this;
    }

    @Override
    public Object datetime() {
        return this;
    }

    @Override
    public Object timestamp() {
        return this;
    }

    @Override
    public Object year() {
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
