package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLUpdateBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineUpdateBuilder {

    protected StampUpdate stampUpdate = new StampUpdate();
    protected List<StampFrom> stampFroms = new ArrayList<>();

    protected StampWhere where = null;
    protected StampWhere lastWhere = null;
    protected List<StampOrderBy> orderBys = new ArrayList<>();
    protected StampOrderBy lastOrderBy = null;
    protected List<StampUpdateItem> items = new ArrayList<>();

    protected StampUpdateItem getLastItem() {
        if (items.size() > 0) {
            return items.get(items.size() - 1);
        }
        return null;
    }

    @Override
    public Object update() {
        this.gammars.add("update");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        stampFroms.add(new StampFrom(table));
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.gammars.add("table");
        stampFroms.add(new StampFrom(table, tableAliasName));
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
        } else if (this.hasPreviousStop("where", "where")) {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(field);

            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        } else if (this.hasPreviousStop("set", "set")) {
            StampUpdateItem item = new StampUpdateItem();
            item.column = new StampColumn(field);
            items.add(item);
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
        } else if (this.hasPreviousStop("where", "where")) {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(table, field);

            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        } else if (this.hasPreviousStop("set", "set")) {
            StampUpdateItem item = new StampUpdateItem();
            item.column = new StampColumn(table, field);
            items.add(item);
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
        } else if (this.hasPreviousStop("where", "where")) {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(aliasName, field);

            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        } else if (this.hasPreviousStop("set", "set")) {
            StampUpdateItem item = new StampUpdateItem();
            item.column = new StampColumn(aliasName, field);
            items.add(item);
        }
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.gammars.add("limit");
        stampUpdate.limit = new StampLimit(pos, len);
        return this;
    }

    @Override
    public Object eq() {
        this.gammars.add("operator");
        if (this.hasPreviousStop("where", "where")) {
            this.lastWhere.operator = "=";
        } else if (this.hasPreviousStop("set", "set")) {

        }
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
    public Object where() {
        this.gammars.add("where");
        return this;
    }

    @Override
    public Object value(Object value) {
        this.gammars.add("value");
        this.getLastItem().value = value;
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
    public StampAction compile() {
        if (stampFroms != null && stampFroms.size() > 0) {
            this.stampUpdate.tables = stampFroms.toArray(new StampFrom[]{});
        }
        if (where != null) stampUpdate.where = where;
        if (orderBys != null) stampUpdate.orderBy = orderBys.toArray(new StampOrderBy[]{});
        if (items != null && items.size() > 0) stampUpdate.items = items.toArray(new StampUpdateItem[]{});
        return this.stampUpdate;
    }
}
