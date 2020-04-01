package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;

public class SimpleCommonWhereBuilder
        extends
        AbstractSQLBuilder
        implements
        AboutChildBuilder,
        WrapperBuilder,
        LogicBuilder,
        OperatorBuilder,
        OperatorLinkBuilder,
        OperatorFunctionBuilder,
        BetweenValueBuilder,
        AbsValueBuilder {

    protected StampWhere where = null;
    protected StampWhere lastWhere = null;

    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        if (this.previous("operator")) {
            this.lastWhere.compareColumn = new StampColumn(field);
        } else {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(field);
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        this.gammars.add("column");
        if (this.previous("operator")) {
            this.lastWhere.compareColumn = new StampColumn(field);
        } else {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(table, field);
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        this.gammars.add("column");
        if (this.previous("operator")) {
            this.lastWhere.compareColumn = new StampColumn(field);
        } else {
            StampWhere where = new StampWhere();
            where.column = new StampColumn(aliasName, field);
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object wrapper(UnifyBuilder builder) {
        this.gammars.add("wrapper");
        if (builder instanceof AboutChildBuilder) {
            StampWhere where = new StampWhere();
            this.lastWhere.next = where;
            this.lastWhere = where;
            StampWhere wrapper = ((AboutChildBuilder) builder).getStampWhere();
            this.lastWhere.wrapWhere = wrapper;
        }
        return this;
    }

    @Override
    public StampWhere getStampWhere() {
        return this.where;
    }

    @Override
    public Object and() {
        this.gammars.add("and");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    @Override
    public Object or() {
        this.gammars.add("or");
        this.lastWhere.nextLogic = KeyLogic.AND;
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }

    @Override
    public Object eq() {
        this.gammars.add("operator");
        this.lastWhere.operator = "=";
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
    public Object section(Serializable valueA, Serializable valueB) {
        this.gammars.add("section");
        this.lastWhere.value = valueA;
        this.lastWhere.value2 = valueB;
        return this;
    }

    @Override
    public Object value(Object value) {
        this.gammars.add("value");
        this.lastWhere.value = value;
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
}
