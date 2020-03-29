package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLSelectBuilder
        implements
        RedefineSelectBuilder {

    @Override
    public Object select() {
        return this;
    }

    @Override
    public Object all() {
        return this;
    }

    @Override
    public Object field(Serializable... fields) {
        return this;
    }

    @Override
    public Object field(Class table, Serializable... fields) {
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable... fields) {
        return this;
    }

    @Override
    public Object field(Serializable field, String fieldAliasName) {
        return this;
    }

    @Override
    public Object field(Class table, Serializable field, String fieldAliasName) {
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable field, String fieldAliasName) {
        return this;
    }

    @Override
    public Object distinct(Serializable field) {
        return this;
    }

    @Override
    public Object distinct(String tableAliasName, Serializable field) {
        return this;
    }

    @Override
    public Object distinct(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object distinct(Serializable field, String fieldAliasName) {
        return this;
    }

    @Override
    public Object distinct(String tableAliasName, Serializable field, String fieldAliasName) {
        return this;
    }

    @Override
    public Object distinct(Class table, Serializable field, String fieldAliasName) {
        return this;
    }

    @Override
    public Object table(Class table) {
        return this;
    }

    @Override
    public Object value(Object value) {
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
    public Object and() {
        return this;
    }

    @Override
    public Object from() {
        return this;
    }

    @Override
    public Object having() {
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
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
    public BetweenValueBuilder notBetween() {
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        return this;
    }

    @Override
    public Object eq() {
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

    @Override
    public Object asc() {
        return this;
    }

    @Override
    public Object desc() {
        return this;
    }

    @Override
    public Object where() {
        return this;
    }

    @Override
    public Object wrapper(AboutChildBuilder builder) {
        return this;
    }

    @Override
    public Object inner() {
        return this;
    }

    @Override
    public Object join() {
        return this;
    }

    @Override
    public Object left() {
        return this;
    }

    @Override
    public Object on() {
        return this;
    }

    @Override
    public Object groupBy() {
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        return this;
    }

    @Override
    public Object as(String tableAliasName) {
        return this;
    }

    @Override
    public Object count(Serializable... param) {
        return this;
    }

    @Override
    public Object max(Serializable... params) {
        return this;
    }

    @Override
    public Object avg(Serializable... params) {
        return this;
    }

    @Override
    public Object sum(Serializable... params) {
        return this;
    }

    @Override
    public Object min(Serializable... params) {
        return this;
    }

    @Override
    public Object concat(Serializable... params) {
        return this;
    }

    @Override
    public Object substring(Serializable param, int pos, int len) {
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
