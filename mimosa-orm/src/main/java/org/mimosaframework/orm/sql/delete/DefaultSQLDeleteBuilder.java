package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLDeleteBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineDeleteBuilder {

    protected StampDelete stampDelete = new StampDelete();
    protected List<StampFrom> stampFroms = new ArrayList<>();
    protected List<Class> deletes = new ArrayList<>();
    protected StampWhere where = null;
    protected StampWhere lastWhere = null;
    protected List<StampOrderBy> orderBys = new ArrayList<>();
    protected StampOrderBy lastOrderBy = null;

    @Override
    public Object delete() {
        this.gammars.add("delete");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        if (this.hasPreviousStop("from", "from")
                || this.hasPreviousStop("using", "using")) {
            StampFrom stampFrom = new StampFrom(table);
            stampFrom.table = table;
            stampFroms.add(stampFrom);
        } else {
            deletes.add(table);
        }
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.gammars.add("table");
        StampFrom stampFrom = new StampFrom(table, tableAliasName);
        stampFrom.table = table;
        stampFrom.aliasName = tableAliasName;
        stampFroms.add(stampFrom);
        return this;
    }

    @Override
    public Object from() {
        this.gammars.add("from");
        return this;
    }

    @Override
    public Object where() {
        this.gammars.add("where");
        return this;
    }

    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        if (this.hasPreviousStop("orderBy", "orderBy")) {
            StampOrderBy stampOrderBy = new StampOrderBy();
            stampOrderBy.column = new StampColumn(field);
            this.lastOrderBy = stampOrderBy;
            this.orderBys.add(stampOrderBy);
        } else {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(field);

            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        this.gammars.add("column");
        if (this.hasPreviousStop("orderBy", "orderBy")) {
            StampOrderBy stampOrderBy = new StampOrderBy();
            stampOrderBy.column = new StampColumn(table, field);
            this.lastOrderBy = stampOrderBy;
            this.orderBys.add(stampOrderBy);
        } else {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(table, field);

            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        this.gammars.add("column");
        if (this.hasPreviousStop("orderBy", "orderBy")) {
            StampOrderBy stampOrderBy = new StampOrderBy();
            stampOrderBy.column = new StampColumn(aliasName, field);
            this.lastOrderBy = stampOrderBy;
            this.orderBys.add(stampOrderBy);
        } else {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(aliasName, field);

            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object eq() {
        this.gammars.add("operator");
        this.lastWhere.operator = "=";
        return this;
    }

    @Override
    public Object value(Object value) {
        this.gammars.add("value");
        this.lastWhere.value = value;
        return this;
    }

    @Override
    public Object in() {
        this.gammars.add("operator");
        this.lastWhere.operator = "in";
        return this;
    }

    @Override
    public Object nin() {
        this.gammars.add("operator");
        this.lastWhere.operator = "not in";
        return this;
    }

    @Override
    public Object like() {
        this.gammars.add("operator");
        this.lastWhere.operator = "like";
        return this;
    }

    @Override
    public Object ne() {
        this.gammars.add("operator");
        this.lastWhere.operator = "!=";
        return this;
    }

    @Override
    public Object gt() {
        this.gammars.add("operator");
        this.lastWhere.operator = ">";
        return this;
    }

    @Override
    public Object gte() {
        this.gammars.add("operator");
        this.lastWhere.operator = ">=";
        return this;
    }

    @Override
    public Object lt() {
        this.gammars.add("operator");
        this.lastWhere.operator = "<";
        return this;
    }

    @Override
    public Object lte() {
        this.gammars.add("operator");
        this.lastWhere.operator = "<=";
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
        this.gammars.add("between");
        this.lastWhere.operator = "between";
        return this;
    }

    @Override
    public BetweenValueBuilder notBetween() {
        this.gammars.add("notBetween");
        this.lastWhere.operator = "not between";
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        this.gammars.add("section");
        this.lastWhere.value = valueA;
        this.lastWhere.value2 = valueB;
        return this;
    }

    @Override
    public Object and() {
        this.gammars.add("and");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.gammars.add("limit");
        this.stampDelete.limit = new StampLimit(pos, len);
        return this;
    }

    @Override
    public Object or() {
        this.gammars.add("or");
        this.lastWhere.nextLogic = KeyLogic.OR;
        return this;
    }

    @Override
    public Object orderBy() {
        this.gammars.add("orderBy");
        return this;
    }

    @Override
    public Object table(String... aliasNames) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object wrapper(AboutChildBuilder builder) {
        this.gammars.add("wrapper");
        return this;
    }

    @Override
    public Object table(Class... table) {
        this.gammars.add("table");
        return this;
    }

    @Override
    public Object asc() {
        this.gammars.add("asc");
        this.lastOrderBy.sortType = KeySortType.ASC;
        return this;
    }

    @Override
    public Object desc() {
        this.gammars.add("desc");
        this.lastOrderBy.sortType = KeySortType.DESC;
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        this.gammars.add("operator");
        this.lastWhere.operator = "isNull";
        this.lastWhere.compareFun = new StampFieldFun("isNull", new StampColumn(field));
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        this.gammars.add("operator");
        this.lastWhere.operator = "isNull";
        this.lastWhere.compareFun = new StampFieldFun("isNull", new StampColumn(table, field));
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        this.lastWhere.operator = "isNull";
        this.lastWhere.compareFun = new StampFieldFun("isNull", new StampColumn(aliasName, field));
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        this.gammars.add("operator");
        this.lastWhere.operator = "isNotNull";
        this.lastWhere.compareFun = new StampFieldFun("not isNull", new StampColumn(field));
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        this.gammars.add("operator");
        this.lastWhere.operator = "isNotNull";
        this.lastWhere.compareFun = new StampFieldFun("not isNull", new StampColumn(table, field));
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        this.lastWhere.operator = "isNotNull";
        this.lastWhere.compareFun = new StampFieldFun("not isNull", new StampColumn(aliasName, field));
        return this;
    }

    @Override
    public Object using() {
        this.gammars.add("using");
        return this;
    }

    @Override
    public StampAction compile() {
        if (stampFroms != null && stampFroms.size() > 0) {
            this.stampDelete.froms = stampFroms.toArray(new StampFrom[]{});
        }
        if (deletes != null && deletes.size() > 0) {
            stampDelete.tables = deletes.toArray(new Class[]{});
        }
        if (where != null) stampDelete.where = where;
        if (orderBys != null) stampDelete.orderBy = orderBys.toArray(new StampOrderBy[]{});
        return this.stampDelete;
    }
}
