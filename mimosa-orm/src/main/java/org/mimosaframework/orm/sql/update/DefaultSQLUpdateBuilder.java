package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLUpdateBuilder
        extends
        CommonOperatorSQLBuilder
        implements
        RedefineUpdateBuilder {

    protected StampUpdate stampUpdate = new StampUpdate();
    protected List<StampFrom> stampFroms = new ArrayList<>();

    protected StampWhere where = null;
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
        this.setPoint("update");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.setPoint("table");
        stampFroms.add(new StampFrom(table));
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.setPoint("table");
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
            if (this.previous("operator")) {
                this.lastWhere.whereType = KeyWhereType.NORMAL;
                this.lastWhere.rightColumn = new StampColumn(field);
            } else {
                StampWhere where = new StampWhere();
                where.leftColumn = new StampColumn(field);

                if (this.lastWhere != null) this.lastWhere.next = where;
                this.lastWhere = where;
                if (this.where == null) this.where = where;
            }
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
            if (this.previous("operator")) {
                this.lastWhere.whereType = KeyWhereType.NORMAL;
                this.lastWhere.rightColumn = new StampColumn(table, field);
            } else {
                StampWhere where = new StampWhere();
                where.leftColumn = new StampColumn(table, field);

                if (this.lastWhere != null) this.lastWhere.next = where;
                this.lastWhere = where;
                if (this.where == null) this.where = where;
            }
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
            if (this.previous("operator")) {
                this.lastWhere.whereType = KeyWhereType.NORMAL;
                this.lastWhere.rightColumn = new StampColumn(aliasName, field);
            } else {
                StampWhere where = new StampWhere();
                where.leftColumn = new StampColumn(aliasName, field);

                if (this.lastWhere != null) this.lastWhere.next = where;
                this.lastWhere = where;
                if (this.where == null) this.where = where;
            }
        } else if (this.hasPreviousStop("set", "set")) {
            StampUpdateItem item = new StampUpdateItem();
            item.column = new StampColumn(aliasName, field);
            items.add(item);
        }
        return this;
    }

    @Override
    public Object eq() {
        if (this.point.equals("set")) {
            return this;
        } else {
            return super.eq();
        }
    }

    @Override
    public Object value(Object value) {
        if (this.point.equals("set")) {
            StampUpdateItem item = this.getLastItem();
            item.value = value;
            return this;
        } else {
            return super.value(value);
        }
    }

    @Override
    public Object limit(int len) {
        this.gammars.add("limit");
        stampUpdate.limit = new StampLimit(0, len);
        return this;
    }

    @Override
    public Object orderBy() {
        this.setPoint("orderBy");
        return this;
    }

    @Override
    public Object set() {
        this.setPoint("set");
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
        this.setPoint("where");
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
