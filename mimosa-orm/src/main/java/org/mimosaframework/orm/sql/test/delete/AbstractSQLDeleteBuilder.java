package org.mimosaframework.orm.sql.test.delete;


import org.mimosaframework.orm.sql.test.*;

import java.io.Serializable;

public class AbstractSQLDeleteBuilder
        implements
        DeleteBuilder,
        AbsTableBuilder,
        FromBuilder,
        WhereBuilder,
        AbsWhereColumnBuilder,
        OperatorBuilder,
        AbsValueBuilder,
        BetweenValueBuilder,
        LogicBuilder,
        OrderByBuilder,
        LimitBuilder {

    @Override
    public Object delete() {
        return this;
    }

    @Override
    public Object table(Class table) {
        return this;
    }

    @Override
    public Object from() {
        return this;
    }

    @Override
    public Object where() {
        return this;
    }

    @Override
    public Object column(Serializable field) {
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        return this;
    }

    @Override
    public Object eq() {
        return this;
    }

    @Override
    public Object value(Object value) {
        return this;
    }

    @Override
    public Object in() {
        return this;
    }

    @Override
    public Object nin() {
        return this;
    }

    @Override
    public Object like() {
        return this;
    }

    @Override
    public Object ne() {
        return this;
    }

    @Override
    public Object gt() {
        return this;
    }

    @Override
    public Object gte() {
        return this;
    }

    @Override
    public Object lt() {
        return this;
    }

    @Override
    public Object lte() {
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        return this;
    }

    @Override
    public Object and() {
        return this;
    }

    @Override
    public Object limit() {
        return this;
    }

    @Override
    public Object or() {
        return this;
    }

    @Override
    public Object orderBy() {
        return this;
    }
}
