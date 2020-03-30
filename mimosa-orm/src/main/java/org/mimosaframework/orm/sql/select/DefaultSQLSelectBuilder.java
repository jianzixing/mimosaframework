package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLSelectBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineSelectBuilder {

    @Override
    public Object select() {
        this.gammars.add("select");
        return this;
    }

    @Override
    public Object all() {
        this.gammars.add("all");
        return this;
    }

    @Override
    public Object field(Serializable... fields) {
        this.gammars.add("field");
        return this;
    }

    @Override
    public Object field(Class table, Serializable... fields) {
        this.gammars.add("field");
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable... fields) {
        this.gammars.add("field");
        return this;
    }

    @Override
    public Object field(Serializable field, String fieldAliasName) {
        this.gammars.add("field");
        return this;
    }

    @Override
    public Object field(Class table, Serializable field, String fieldAliasName) {
        this.gammars.add("field");
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable field, String fieldAliasName) {
        this.gammars.add("field");
        return this;
    }

    @Override
    public Object distinct(Serializable field) {
        this.gammars.add("distinct");
        return this;
    }

    @Override
    public Object distinct(String tableAliasName, Serializable field) {
        this.gammars.add("distinct");
        return this;
    }

    @Override
    public Object distinct(Class table, Serializable field) {
        this.gammars.add("distinct");
        return this;
    }

    @Override
    public Object distinct(Serializable field, String fieldAliasName) {
        this.gammars.add("distinct");
        return this;
    }

    @Override
    public Object distinct(String tableAliasName, Serializable field, String fieldAliasName) {
        this.gammars.add("distinct");
        return this;
    }

    @Override
    public Object distinct(Class table, Serializable field, String fieldAliasName) {
        this.gammars.add("distinct");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object value(Object value) {
        this.gammars.add("value");
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
    public Object and() {
        this.gammars.add("and");
        return this;
    }

    @Override
    public Object from() {
        this.gammars.add("from");
        return this;
    }

    @Override
    public Object having() {
        this.gammars.add("having");
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.gammars.add("limit");
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
    public Object eq() {
        this.gammars.add("operator");
        return this;
    }

    @Override
    public Object or() {
        this.gammars.add("or");
        return this;
    }

    @Override
    public Object orderBy() {
        this.gammars.add("orderBy");
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
    public Object wrapper(AboutChildBuilder builder) {
        this.gammars.add("wrapper");
        return this;
    }

    @Override
    public Object inner() {
        this.gammars.add("inner");
        return this;
    }

    @Override
    public Object join() {
        this.gammars.add("join");
        return this;
    }

    @Override
    public Object left() {
        this.gammars.add("left");
        return this;
    }

    @Override
    public Object on() {
        this.gammars.add("on");
        return this;
    }

    @Override
    public Object groupBy() {
        this.gammars.add("groupBy");
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object as(String tableAliasName) {
        this.gammars.add("create");
        return this;
    }

    @Override
    public Object count(Serializable... param) {
        this.gammars.add("count");
        return this;
    }

    @Override
    public Object max(Serializable... params) {
        this.gammars.add("max");
        return this;
    }

    @Override
    public Object avg(Serializable... params) {
        this.gammars.add("avg");
        return this;
    }

    @Override
    public Object sum(Serializable... params) {
        this.gammars.add("sum");
        return this;
    }

    @Override
    public Object min(Serializable... params) {
        this.gammars.add("min");
        return this;
    }

    @Override
    public Object concat(Serializable... params) {
        this.gammars.add("concat");
        return this;
    }

    @Override
    public Object substring(Serializable param, int pos, int len) {
        this.gammars.add("substring");
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
