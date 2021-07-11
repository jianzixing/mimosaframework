package org.mimosaframework.orm.sql.update;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.sql.CommonOperatorSQLBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLUpdateBuilder
        extends
        CommonOperatorSQLBuilder<DefaultSQLUpdateBuilder>
        implements
        RedefineUpdateBuilder {

    protected StampUpdate stampUpdate = new StampUpdate();

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
    public DefaultSQLUpdateBuilder update() {
        this.addPoint("update");
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder table(Class table) {
        this.gammars.add("table");
        stampUpdate.table = new StampFrom(table);
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder table(String name) {
        this.gammars.add("table");
        stampUpdate.table = new StampFrom(name);
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder column(Serializable field) {
        this.gammars.add("column");
        this.column(null, null, field);
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder column(Class table, Serializable field) {
        this.gammars.add("column");
        this.column(null, table, field);
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder column(String aliasName, Serializable field) {
        this.gammars.add("column");
        this.column(aliasName, null, field);
        return this;
    }

    protected void column(String aliasName, Class table, Serializable field) {
        StampColumn column = null;
        if (StringTools.isNotEmpty(aliasName)) {
            column = new StampColumn(aliasName, field);
        } else if (table != null) {
            column = new StampColumn(table, field);
        } else {
            column = new StampColumn(field);
        }
        if (this.point.equals("orderBy")) {
            StampOrderBy stampOrderBy = new StampOrderBy();
            stampOrderBy.column = column;
            this.lastOrderBy = stampOrderBy;
            this.orderBys.add(stampOrderBy);
        } else if (this.point.equals("where")) {
            if (this.previous("operator")) {
                this.lastWhere.whereType = KeyWhereType.NORMAL;
                this.lastWhere.rightColumn = column;
            } else {
                StampWhere where = new StampWhere();
                where.leftColumn = column;

                if (this.lastWhere != null) this.lastWhere.next = where;
                this.lastWhere = where;
                if (this.where == null) this.where = where;
            }
        }
    }

    @Override
    public DefaultSQLUpdateBuilder and() {
        this.gammars.add("and");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder or() {
        this.gammars.add("or");
        this.lastWhere.nextLogic = KeyLogic.OR;
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder eq() {
        if (this.point.equals("set")) {
            return this;
        } else {
            return super.eq();
        }
    }

    @Override
    public DefaultSQLUpdateBuilder value(Object value) {
        if (this.point.equals("set")) {
            StampUpdateItem item = this.getLastItem();
            item.value = value;
            return this;
        } else {
            return super.value(value);
        }
    }

    public DefaultSQLUpdateBuilder limit(int len) {
        // this.gammars.add("limit");
        // stampUpdate.limit = new StampLimit(0, len);
        // 不支持删除指定行数
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder orderBy() {
        this.addPoint("orderBy");
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder asc() {
        this.gammars.add("asc");
        this.lastOrderBy.sortType = KeySortType.ASC;
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder desc() {
        this.gammars.add("desc");
        this.lastOrderBy.sortType = KeySortType.DESC;
        return this;
    }

    @Override
    public DefaultSQLUpdateBuilder where() {
        this.addPoint("where");
        return this;
    }

    @Override
    public StampUpdate compile() {
        if (where != null) stampUpdate.where = where;
        if (items != null && items.size() > 0) stampUpdate.items = items.toArray(new StampUpdateItem[]{});
        return this.stampUpdate;
    }

    @Override
    public DefaultSQLUpdateBuilder set(Serializable field, Object value) {
        this.addPoint("set");
        StampColumn column = new StampColumn(field);
        StampUpdateItem item = new StampUpdateItem();
        item.column = column;
        item.value = value;
        items.add(item);
        return this;
    }
}
