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

    protected StampDelete stampDelete = new StampDelete();
    protected StampFrom stampFrom;
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
        if (this.point.equals("from")) {
            this.stampFrom = new StampFrom(table);
            this.stampFrom.table = table;
        }
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
    public StampAction compile() {
        this.stampDelete.from = stampFrom;
        if (where != null) stampDelete.where = where;
        return this.stampDelete;
    }
}
