package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.stamp.KeyWhereType;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampFieldFun;
import org.mimosaframework.orm.sql.stamp.StampWhere;

import java.io.Serializable;

public abstract class CommonOperatorSQLBuilder<T extends CommonOperatorSQLBuilder>
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
    public T wrapper(UnifyBuilder builder) {
        this.gammars.add("wrapper");
        if (builder instanceof AboutChildBuilder) {
            StampWhere where = new StampWhere();
            this.lastWhere.next = where;
            this.lastWhere = where;
            this.lastWhere.whereType = KeyWhereType.WRAP;
            this.lastWhere.wrapWhere = ((AboutChildBuilder) builder).getStampWhere();
        }
        return (T) this;
    }

    @Override
    public T eq() {
        this.gammars.add("operator");
        this.lastWhere.operator = "=";
        return (T) this;
    }

    @Override
    public T in() {
        this.gammars.add("operator");
        this.lastWhere.operator = "in";
        return (T) this;
    }

    @Override
    public T nin() {
        this.gammars.add("operator");
        this.lastWhere.not = true;
        this.lastWhere.operator = "in";
        return (T) this;
    }

    @Override
    public T like() {
        this.gammars.add("operator");
        this.lastWhere.operator = "like";
        return (T) this;
    }

    @Override
    public T ne() {
        this.gammars.add("operator");
        this.lastWhere.operator = "!=";
        return (T) this;
    }

    @Override
    public T gt() {
        this.gammars.add("operator");
        this.lastWhere.operator = ">";
        return (T) this;
    }

    @Override
    public T gte() {
        this.gammars.add("operator");
        this.lastWhere.operator = ">=";
        return (T) this;
    }

    @Override
    public T lt() {
        this.gammars.add("operator");
        this.lastWhere.operator = "<";
        return (T) this;
    }

    @Override
    public T lte() {
        this.gammars.add("operator");
        this.lastWhere.operator = "<=";
        return (T) this;
    }

    @Override
    public T between() {
        this.gammars.add("between");
        this.lastWhere.operator = "between";
        this.lastWhere.whereType = KeyWhereType.KEY_AND;
        return (T) this;
    }

    @Override
    public T notBetween() {
        this.gammars.add("notBetween");
        this.lastWhere.not = true;
        this.lastWhere.operator = "between";
        this.lastWhere.whereType = KeyWhereType.KEY_AND;
        return (T) this;
    }

    @Override
    public T section(Serializable valueA, Serializable valueB) {
        this.gammars.add("section");
        this.lastWhere.rightValue = valueA;
        this.lastWhere.rightValueEnd = valueB;
        return (T) this;
    }

    @Override
    public T value(Object value) {
        this.gammars.add("value");
        this.lastWhere.whereType = KeyWhereType.NORMAL;
        this.lastWhere.rightValue = value;
        return (T) this;
    }

    @Override
    public T isNull(Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(field));
        return (T) this;
    }

    @Override
    public T isNull(Class table, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(table, field));
        return (T) this;
    }

    @Override
    public T isNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(aliasName, field));
        return (T) this;
    }

    @Override
    public T isNotNull(Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.not = true;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(field));
        return (T) this;
    }

    @Override
    public T isNotNull(Class table, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.not = true;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(table, field));
        return (T) this;
    }

    @Override
    public T isNotNull(String aliasName, Serializable field) {
        this.gammars.add("operator");
        StampWhere where = new StampWhere();
        this.lastWhere.next = where;
        this.lastWhere = where;
        this.lastWhere.whereType = KeyWhereType.FUN;
        this.lastWhere.not = true;
        this.lastWhere.fun = new StampFieldFun("isNull", new StampColumn(aliasName, field));
        return (T) this;
    }
}
