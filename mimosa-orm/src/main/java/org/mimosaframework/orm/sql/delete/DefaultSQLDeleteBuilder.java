package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLDeleteBuilder
        extends
        CommonOperatorSQLBuilder
        implements
        RedefineDeleteBuilder {

    protected boolean isUsing = false;

    protected StampDelete stampDelete = new StampDelete();
    protected List<StampFrom> stampFroms = new ArrayList<>();
    protected StampWhere where = null;
    protected List<StampOrderBy> orderBys = new ArrayList<>();
    protected StampOrderBy lastOrderBy = null;

    @Override
    public Object delete() {
        this.addPoint("delete");
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        if (this.previous("delete")) {
            this.stampDelete.delTable = table;
        } else if (this.point.equals("from") || this.point.equals("using")) {
            StampFrom stampFrom = new StampFrom(table);
            stampFrom.table = table;
            stampFroms.add(stampFrom);
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
        this.addPoint("from");
        return this;
    }

    @Override
    public Object where() {
        this.addPoint("where");
        return this;
    }

    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        this.column(null, null, field);
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        this.gammars.add("column");
        this.column(null, table, field);
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        this.gammars.add("column");
        this.column(aliasName, null, field);
        return this;
    }

    private void column(String aliasName, Class table, Serializable field) {
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
        } else {
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
    public Object and() {
        this.gammars.add("and");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    public Object limit(int len) {
        // this.addPoint("limit");
        // this.stampDelete.limit = new StampLimit(0, len);
        // 不支持删除指定行数
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
        this.addPoint("orderBy");
        return this;
    }

    @Override
    public Object table(String aliasName) {
        this.gammars.add("table");
        this.stampDelete.delTableAlias = aliasName;
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
    public Object using() {
        this.addPoint("using");
        this.isUsing = true;
        if (stampFroms.size() > 0) {
            StampFrom from = stampFroms.get(stampFroms.size() - 1);
            if (from.table != null) {
                this.stampDelete.delTable = from.table;
            } else if (StringTools.isNotEmpty(from.aliasName)) {
                this.stampDelete.delTableAlias = from.aliasName;
            }
        }
        return this;
    }

    @Override
    public StampAction compile() {
        if (stampFroms != null && stampFroms.size() > 0) {
            this.stampDelete.froms = stampFroms.toArray(new StampFrom[]{});
        }
        if (where != null) stampDelete.where = where;
        if (orderBys != null) stampDelete.orderBy = orderBys.toArray(new StampOrderBy[]{});
        return this.stampDelete;
    }
}
