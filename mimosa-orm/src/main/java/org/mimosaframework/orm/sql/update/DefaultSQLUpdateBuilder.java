package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLUpdateBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineUpdateBuilder {

    @Override
    public Object update() {
        this.gammars.add("update");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        this.gammars.add("column");
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        this.gammars.add("column");
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.gammars.add("limit");
        return this;
    }

    @Override
    public Object eq() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object orderBy() {
        this.gammars.add("orderBy");
        return this;
    }

    @Override
    public Object set() {
        this.gammars.add("set");
        return this;
    }

    @Override
    public Object asc() {
        this.gammars.add("asc");
        return this;
    }

    @Override
    public Object desc() {
        this.gammars.add("desc");
        return this;
    }

    @Override
    public Object where() {
        this.gammars.add("where");
        return this;
    }

    @Override
    public Object value(Object value) {
        this.gammars.add("value");
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object in() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object nin() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object like() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object ne() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object gt() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object gte() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object lt() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object lte() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
        this.gammars.add("between");
        return this;
    }

    @Override
    public BetweenValueBuilder notBetween() {
        this.gammars.add("notBetween");
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        this.gammars.add("section");
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
