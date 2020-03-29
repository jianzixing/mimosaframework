package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLAlterBuilder
        extends
        AbstractSQLBuilder
        implements

        RedefineAlterBuilder {

    @Override
    public Object alter() {
        return this;
    }

    @Override
    public Object name(Serializable value) {
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
    public Object database() {
        this.body = 1;
        return this;
    }

    @Override
    public Object column(Serializable field) {

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
    public Object after() {
        return this;
    }

    @Override
    public Object autoIncrement() {
        return this;
    }

    @Override
    public Object column() {
        return this;
    }

    @Override
    public Object add() {
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
    public Object comment(String comment) {
        return this;
    }

    @Override
    public Object index() {
        return this;
    }

    @Override
    public Object split() {
        return this;
    }

    @Override
    public Object drop() {
        return this;
    }

    @Override
    public Object key() {
        return this;
    }

    @Override
    public Object primary() {
        return this;
    }

    @Override
    public Object to() {
        return this;
    }

    @Override
    public Object change() {
        return this;
    }

    @Override
    public Object modify() {
        return this;
    }

    @Override
    public Object newColumn(Serializable field) {
        return this;
    }

    @Override
    public Object oldColumn(Serializable field) {
        return this;
    }

    @Override
    public Object rename() {
        return this;
    }

    @Override
    public Object fullText() {
        return this;
    }

    @Override
    public Object unique() {
        return this;
    }

    @Override
    public Object value(int number) {
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
