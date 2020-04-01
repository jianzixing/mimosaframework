package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.stamp.KeyWhereType;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampFieldFun;
import org.mimosaframework.orm.sql.stamp.StampWhere;

import java.io.Serializable;

public abstract class CommonOperatorSQLBuilder
        extends
        AbstractSQLBuilder
        implements
        OperatorBuilder,
        WrapperBuilder,
        BetweenValueBuilder,
        AbsValueBuilder,
        OperatorFunctionBuilder {

    protected StampWhere lastWhere = null;

    @Override
    public Object wrapper(UnifyBuilder builder) {
        this.gammars.add("wrapper");
        if (builder instanceof AboutChildBuilder) {
            StampWhere where = new StampWhere();
            this.lastWhere.next = where;
            this.lastWhere = where;
            this.lastWhere.whereType = KeyWhereType.WRAP;
            this.lastWhere.wrapWhere = ((AboutChildBuilder) builder).getStampWhere();
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
    public Object in() {
        this.gammars.add("operator");
        this.lastWhere.operator = "in";
        return this;
    }

    @Override
    public Object nin() {
        this.gammars.add("operator");
        this.lastWhere.not = true;
        this.lastWhere.operator = "in";
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
        this.lastWhere.not = true;
        this.lastWhere.operator = "between";
        return this;
    }

    @Override
    public Object section(Serializable valueA, Serializable valueB) {
        this.gammars.add("section");
        this.lastWhere.rightValue = valueA;
        this.lastWhere.rightValueEnd = valueB;
        return this;
    }

    @Override
    public Object value(Object value) {
        this.gammars.add("value");
        this.lastWhere.whereType = KeyWhereType.NORMAL;
        this.lastWhere.rightValue = value;
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(field));
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(table, field));
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(aliasName, field));
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.not = true;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(field));
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.not = true;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(table, field));
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.not = true;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(aliasName, field));
        return this;
    }
}
