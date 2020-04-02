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
    protected List<Class> deletes = new ArrayList<>();
    protected StampWhere where = null;
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
        }
        return this;
    }

    @Override
    public Object and() {
        this.gammars.add("and");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    @Override
    public Object limit(int len) {
        this.gammars.add("limit");
        this.stampDelete.limit = new StampLimit(0, len);
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
        this.stampDelete.delTableAlias = aliasNames;
        return this;
    }

    @Override
    public Object table(Class... table) {
        this.gammars.add("table");
        if (this.previous("delete")) {
            this.stampDelete.delTables = table;
        }
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
        this.gammars.add("using");
        this.isUsing = true;
        if (stampFroms.size() > 0) {
            StampFrom from = stampFroms.get(stampFroms.size() - 1);
            if (from.table != null) {
                this.stampDelete.delTables = new Class[]{from.table};
            } else if (StringTools.isNotEmpty(from.aliasName)) {
                this.stampDelete.delTableAlias = new String[]{from.aliasName};
            }
        }
        return this;
    }

    @Override
    public StampAction compile() {
        if (stampFroms != null && stampFroms.size() > 0) {
            this.stampDelete.froms = stampFroms.toArray(new StampFrom[]{});
        }
        if (deletes != null && deletes.size() > 0) {
            stampDelete.delTables = deletes.toArray(new Class[]{});
        }
        if (where != null) stampDelete.where = where;
        if (orderBys != null) stampDelete.orderBy = orderBys.toArray(new StampOrderBy[]{});
        return this.stampDelete;
    }
}
