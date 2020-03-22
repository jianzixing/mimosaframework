package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public class AbstractSQLSelectBuilder
        extends
        AbstractSQLBuilder

        implements
        SelectBuilder,
        AbsFieldBuilder,
        FromBuilder,
        AbsTablesBuilder,
        WhereBuilder,
        AbsWhereColumnBuilder,
        OperatorFunctionBuilder,
        LogicBuilder,
        WrapperBuilder,
        HavingBuilder,
        FieldFunBuilder,
        AbsValueBuilder,
        OrderByBuilder,
        SortBuilder,
        LimitBuilder,
        AbsColumnBuilder,
        BetweenValueBuilder,
        SplitBuilder,

        LeftBuilder,
        InnerBuilder,
        JoinBuilder,
        OnBuilder,
        AbsTableBuilder {

    @Override
    protected SQLBuilder createSQLBuilder() {
        return null;
    }

    @Override
    public MappingTable getMappingTableByClass(Class table) {
        return null;
    }

    @Override
    public Object select() {
        this.sqlBuilder.SELECT();
        return this;
    }

    @Override
    public Object all() {
        return this;
    }

    @Override
    public Object fields(Class table, Object... fields) {
        return this;
    }

    @Override
    public Object fields(FieldItem... fieldItems) {
        return this;
    }

    @Override
    public Object fields(Fields fieldItems) {
        return this;
    }

    @Override
    public Object table(Class[] table) {
        return this;
    }

    @Override
    public Object table(TableItem tableItem) {
        return this;
    }

    @Override
    public Object table(TableItem... tableItem) {
        return this;
    }

    @Override
    public Object table(TableItems tableItems) {
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
    public Object count(FieldItem fieldItem) {
        return this;
    }

    @Override
    public Object max(FieldItem fieldItem) {
        return this;
    }

    @Override
    public Object avg(FieldItem fieldItem) {
        return this;
    }

    @Override
    public Object sum(FieldItem fieldItem) {
        return this;
    }

    @Override
    public Object min(FieldItem fieldItem) {
        return this;
    }

    @Override
    public Object concat(FieldItem... fieldItem) {
        return this;
    }

    @Override
    public Object substring(FieldItem fieldItem, int pos, int len) {
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
    public BetweenValueBuilder notBetween() {
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
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
    public Object section(Object valueA, Object valueB) {
        return this;
    }

    @Override
    public Object split() {
        this.sqlBuilder.addSplit();
        return this;
    }
}
