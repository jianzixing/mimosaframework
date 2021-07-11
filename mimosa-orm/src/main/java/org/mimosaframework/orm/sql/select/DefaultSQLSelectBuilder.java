package org.mimosaframework.orm.sql.select;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLSelectBuilder
        extends
        CommonOperatorSQLBuilder<DefaultSQLSelectBuilder>
        implements
        RedefineSelectBuilder {

    protected StampSelect stampSelect = new StampSelect();
    protected List<StampSelectField> stampSelectFields = new ArrayList<>();
    protected List<StampFrom> stampFroms = new ArrayList<>();

    protected List<StampSelectJoin> joins = new ArrayList<>();
    protected StampSelectJoin lastJoin = null;

    protected StampWhere having = null;
    protected List<StampOrderBy> orderBys = new ArrayList<>();
    protected StampOrderBy lastOrderBy = null;
    protected List<StampColumn> groupBy = new ArrayList<>();

    protected StampSelectField getLastField() {
        if (this.stampSelectFields.size() > 0) {
            return this.stampSelectFields.get(this.stampSelectFields.size() - 1);
        }
        return null;
    }

    @Override
    public DefaultSQLSelectBuilder select() {
        this.addPoint("select");
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder all() {
        this.gammars.add("all");
        StampSelectField field = new StampSelectField();
        field.fieldType = KeyFieldType.ALL;
        stampSelectFields.add(field);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder fields(Serializable... fields) {
        this.gammars.add("field");
        int i = 0;
        for (Serializable f : fields) {
            StampSelectField field = new StampSelectField();
            field.fieldType = KeyFieldType.COLUMN;
            field.column = new StampColumn(f);
            stampSelectFields.add(field);
            if (i == 0) {
                this.setKeyword2SelectField(field);
            }
            i++;
        }
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder fields(Class table, Serializable... fields) {
        this.gammars.add("field");
        int i = 0;
        for (Serializable f : fields) {
            StampSelectField field = new StampSelectField();
            field.fieldType = KeyFieldType.COLUMN;
            field.column = new StampColumn(table, f);
            stampSelectFields.add(field);
            if (i == 0) {
                this.setKeyword2SelectField(field);
            }
            i++;
        }
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder fields(String tableAliasName, Serializable... fields) {
        this.gammars.add("field");
        int i = 0;
        for (Serializable f : fields) {
            StampSelectField field = new StampSelectField();
            field.fieldType = KeyFieldType.COLUMN;
            field.column = new StampColumn(tableAliasName, f);
            stampSelectFields.add(field);
            if (i == 0) {
                this.setKeyword2SelectField(field);
            }
            i++;
        }
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder field(Serializable field, String fieldAliasName) {
        this.gammars.add("field");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.aliasName = fieldAliasName;
        selectField.column = new StampColumn(field);
        stampSelectFields.add(selectField);
        this.setKeyword2SelectField(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder field(Class table, Serializable field, String fieldAliasName) {
        this.gammars.add("field");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.aliasName = fieldAliasName;
        selectField.column = new StampColumn(table, field);
        stampSelectFields.add(selectField);
        this.setKeyword2SelectField(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder field(String tableAliasName, Serializable field, String fieldAliasName) {
        this.gammars.add("field");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.aliasName = fieldAliasName;
        selectField.column = new StampColumn(tableAliasName, field);
        stampSelectFields.add(selectField);
        this.setKeyword2SelectField(selectField);
        return this;
    }

    private void setKeyword2SelectField(StampSelectField selectField) {
        if (this.previous("distinct")) {
            // selectField.distinct = true;
        }
    }

    @Override
    public DefaultSQLSelectBuilder distinct(Serializable field) {
        this.gammars.add("distinct");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.column = new StampColumn(field);
        selectField.distinct = true;
        stampSelectFields.add(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder distinctByAlias(String tableAliasName, Serializable field) {
        this.gammars.add("distinct");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.column = new StampColumn(tableAliasName, field);
        selectField.distinct = true;
        stampSelectFields.add(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder distinct(Class table, Serializable field) {
        this.gammars.add("distinct");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.column = new StampColumn(table, field);
        selectField.distinct = true;
        stampSelectFields.add(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder distinct(Serializable field, String fieldAliasName) {
        this.gammars.add("distinct");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.aliasName = fieldAliasName;
        selectField.column = new StampColumn(field);
        selectField.distinct = true;
        stampSelectFields.add(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder distinct(String tableAliasName, Serializable field, String fieldAliasName) {
        this.gammars.add("distinct");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.aliasName = fieldAliasName;
        selectField.column = new StampColumn(tableAliasName, field);
        selectField.distinct = true;
        stampSelectFields.add(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder distinct(Class table, Serializable field, String fieldAliasName) {
        this.gammars.add("distinct");
        StampSelectField selectField = new StampSelectField();
        selectField.fieldType = KeyFieldType.COLUMN;
        selectField.aliasName = fieldAliasName;
        selectField.column = new StampColumn(table, field);
        selectField.distinct = true;
        stampSelectFields.add(selectField);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder column(Serializable field) {
        this.gammars.add("column");
        this.column(null, null, field);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder column(Class table, Serializable field) {
        this.gammars.add("column");
        this.column(null, table, field);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder column(String aliasName, Serializable field) {
        this.gammars.add("column");
        this.column(aliasName, null, field);
        return this;
    }

    protected void column(String aliasName, Class table, Serializable field) {
        StampColumn column = null;
        if (table != null) column = new StampColumn(table, field);
        else if (aliasName != null) column = new StampColumn(aliasName, field);
        else column = new StampColumn(field);

        if (this.point.equals("orderBy")) {
            StampOrderBy stampOrderBy = new StampOrderBy();
            stampOrderBy.column = column;
            this.lastOrderBy = stampOrderBy;
            this.orderBys.add(stampOrderBy);
        } else if (this.point.equals("groupBy")) {
            this.groupBy.add(column);
        } else if (this.point.equals("having")) {
            if (this.previous("operator")) {
                this.lastWhere.whereType = KeyWhereType.NORMAL;
                this.lastWhere.rightColumn = column;
            } else {
                StampWhere where = new StampWhere();
                where.leftColumn = column;

                if (this.lastWhere != null) this.lastWhere.next = where;
                this.lastWhere = where;
            }
        } else {
            if (this.previous("operator")) {
                this.lastWhere.whereType = KeyWhereType.NORMAL;
                this.lastWhere.rightColumn = column;
            } else {
                StampWhere where = new StampWhere();
                where.leftColumn = column;

                if (this.lastWhere != null) this.lastWhere.next = where;
                this.lastWhere = where;
            }
        }
    }

    @Override
    public DefaultSQLSelectBuilder and() {
        this.gammars.add("and");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder from() {
        this.addPoint("from");
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder having() {
        this.addPoint("having");
        StampWhere where = new StampWhere();
        this.having = where;
        this.lastWhere = where;
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder limit(long pos, long len) {
        this.addPoint("limit");
        this.stampSelect.limit = new StampLimit(pos, len);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder or() {
        this.gammars.add("or");
        this.lastWhere.nextLogic = KeyLogic.OR;
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder orderBy() {
        this.addPoint("orderBy");
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder asc() {
        this.gammars.add("asc");
        this.lastOrderBy.sortType = KeySortType.ASC;
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder desc() {
        this.gammars.add("desc");
        this.lastOrderBy.sortType = KeySortType.DESC;
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder where() {
        this.addPoint("where");
        StampWhere where = new StampWhere();
        this.where = where;
        this.lastWhere = where;
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder inner() {
        this.gammars.add("inner");
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder join() {
        this.addPoint("join");
        if (this.previous("inner")) {
            this.lastJoin = new StampSelectJoin();
            this.lastJoin.joinType = KeyJoinType.INNER;
            this.joins.add(this.lastJoin);
        }
        if (this.previous("left")) {
            this.lastJoin = new StampSelectJoin();
            this.lastJoin.joinType = KeyJoinType.LEFT;
            this.joins.add(this.lastJoin);
        }
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder left() {
        this.gammars.add("left");
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder on() {
        this.addPoint("on");
        if (this.getPrePoint().equals("join")) {
            this.lastWhere = new StampWhere();
            if (this.lastJoin.on == null) this.lastJoin.on = this.lastWhere;
        }
        return this;
    }

    /**
     * 这里校验必须查询字段已groupBy中的字段开始
     * 并且包含groupBy中所有的字段
     *
     * @return
     */
    @Override
    public DefaultSQLSelectBuilder groupBy() {
        this.addPoint("groupBy");
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder table(Class table) {
        this.table(table, null);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder table(Class table, String tableAliasName) {
        this.gammars.add("table");
        if (this.point.equals("join") && this.gammars.get(this.posPoint - 1).equals("inner")) {
            this.lastJoin.tableClass = table;
            if (StringTools.isNotEmpty(tableAliasName)) this.lastJoin.tableAliasName = tableAliasName;
        } else if (this.point.equals("join") && this.gammars.get(this.posPoint - 1).equals("left")) {
            this.lastJoin.tableClass = table;
            if (StringTools.isNotEmpty(tableAliasName)) this.lastJoin.tableAliasName = tableAliasName;
        } else {
            this.stampFroms.add(new StampFrom(table, tableAliasName));
        }
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder as(String aliasName) {
        this.gammars.add("as");
        if (this.previous("fun")) {
            this.getLastField().aliasName = aliasName;
        }
        return this;
    }

    protected Object[] covertFunParam(Serializable... params) {
        if (params != null) {
            Object[] newParams = new Object[params.length];
            int i = 0;
            for (Serializable param : params) {
                if (param != null) {
                    if (param instanceof FieldItem) {
                        StampColumn column = new StampColumn();
                        column.table = ((FieldItem) param).getTable();
                        column.column = ((FieldItem) param).getField();
                        column.tableAliasName = ((FieldItem) param).getTableAliasName();
                        newParams[i] = column;
                    } else if (param instanceof FunItem) {
                        newParams[i] = new StampFieldFun(((FunItem) param).getFunName(), ((FunItem) param).getParams());
                    } else if ("distinct".equalsIgnoreCase(param.toString())) {
                        StampKeyword stampKeyword = new StampKeyword();
                        stampKeyword.distinct = true;
                        newParams[i] = stampKeyword;
                    } else if (param instanceof String || param.getClass().isEnum()) {
                        newParams[i] = new StampColumn(param);
                    } else {
                        newParams[i] = param;
                    }
                }
                i++;
            }
            return newParams;
        }
        return null;
    }

    protected void commonFunWhere(String funName, Serializable... params) {
        StampFieldFun fun = new StampFieldFun(funName, this.covertFunParam(params));
        if (this.point.equals("having")) {
            if (this.previous("operator")) {
                this.lastWhere.rightFun = fun;
            } else {
                if (this.previous("or") || this.previous("and")) {
                    StampWhere where = new StampWhere();
                    where.leftFun = fun;
                    this.lastWhere.next = where;
                    this.lastWhere = where;
                } else {
                    this.lastWhere.leftFun = fun;
                }
            }
        } else {
            StampSelectField field = new StampSelectField();
            field.fun = fun;
            field.fieldType = KeyFieldType.FUN;
            this.stampSelectFields.add(field);
        }
    }

    @Override
    public DefaultSQLSelectBuilder count(Serializable... params) {
        this.gammars.add("fun");
        this.commonFunWhere("COUNT", params);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder max(Serializable... params) {
        this.gammars.add("fun");
        this.commonFunWhere("MAX", params);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder avg(Serializable... params) {
        this.gammars.add("fun");
        this.commonFunWhere("AVG", params);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder sum(Serializable... params) {
        this.gammars.add("fun");
        this.commonFunWhere("SUM", params);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder min(Serializable... params) {
        this.gammars.add("fun");
        this.commonFunWhere("MIN", params);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder concat(Serializable... params) {
        this.gammars.add("fun");
        this.commonFunWhere("CONCAT", params);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder substring(Serializable param, int pos, int len) {
        this.gammars.add("fun");
        this.commonFunWhere("SUBSTRING", param, pos, len);
        return this;
    }

    @Override
    public StampSelect compile() {
        if (stampSelectFields != null && stampSelectFields.size() > 0) {
            this.stampSelect.columns = stampSelectFields.toArray(new StampSelectField[]{});
        }
        if (stampFroms != null && stampFroms.size() > 0) {
            this.stampSelect.froms = this.stampFroms.toArray(new StampFrom[]{});
        }
        if (joins != null && joins.size() > 0) {
            this.stampSelect.joins = this.joins.toArray(new StampSelectJoin[]{});
        }
        if (this.where != null) {
            this.stampSelect.where = where;
        }
        if (this.having != null) {
            this.stampSelect.having = having;
        }
        if (this.orderBys != null && this.orderBys.size() > 0) {
            this.stampSelect.orderBy = this.orderBys.toArray(new StampOrderBy[]{});
        }
        if (this.groupBy != null && this.groupBy.size() > 0) {
            this.stampSelect.groupBy = this.groupBy.toArray(new StampColumn[]{});
        }
        return this.stampSelect;
    }

    @Override
    public DefaultSQLSelectBuilder table(String tableName) {
        this.table(tableName, null);
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder table(String tableName, String tableAliasName) {
        this.gammars.add("table");
        if (this.point.equals("join") && this.gammars.get(this.posPoint - 1).equals("inner")) {
            this.lastJoin.tableName = tableName;
            if (StringTools.isNotEmpty(tableAliasName)) this.lastJoin.tableAliasName = tableAliasName;
        } else if (this.point.equals("join") && this.gammars.get(this.posPoint - 1).equals("left")) {
            this.lastJoin.tableName = tableName;
            if (StringTools.isNotEmpty(tableAliasName)) this.lastJoin.tableAliasName = tableAliasName;
        } else {
            this.stampFroms.add(new StampFrom(tableName, tableAliasName));
        }
        return this;
    }

    @Override
    public DefaultSQLSelectBuilder table(UnifyBuilder builder) {
        this.table(builder, null);
        return this;
    }

    @Override
    public Object table(UnifyBuilder builder, String tableAliasName) {
        if (builder instanceof SelectBuilder) {
            this.gammars.add("table");
            if (this.point.equals("join") && this.gammars.get(this.posPoint - 1).equals("inner")) {
                this.lastJoin.builder = builder;
                if (StringTools.isNotEmpty(tableAliasName)) {
                    this.lastJoin.tableAliasName = tableAliasName;
                }
            } else if (this.point.equals("join") && this.gammars.get(this.posPoint - 1).equals("left")) {
                this.lastJoin.builder = builder;
                if (StringTools.isNotEmpty(tableAliasName)) {
                    this.lastJoin.tableAliasName = tableAliasName;
                }
            } else {
                StampFrom from = new StampFrom((StampSelect) builder.compile());
                this.stampFroms.add(from);
                if (StringTools.isNotEmpty(tableAliasName)) {
                    from.aliasName = tableAliasName;
                }
            }
        } else {
            throw new IllegalArgumentException(I18n.print("select_from_select_must"));
        }
        return this;
    }

    public Object forUpdate() {
        this.stampSelect.forUpdate = true;
        return this;
    }
}
