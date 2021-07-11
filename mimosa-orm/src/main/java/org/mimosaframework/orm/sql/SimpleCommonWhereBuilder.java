package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;

public class SimpleCommonWhereBuilder
        extends
        CommonOperatorSQLBuilder
        implements
        CommonSymbolBuilder,
        AboutChildBuilder,
        WrapperBuilder,
        LogicBuilder,
        OperatorBuilder,
        OperatorLinkBuilder,
        OperatorFunctionBuilder,
        BetweenValueBuilder,
        AbsValueBuilder {

    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
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
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        this.gammars.add("column");
        if (this.previous("operator")) {
            this.lastWhere.whereType = KeyWhereType.NORMAL;
            this.lastWhere.rightColumn = new StampColumn(field);
        } else {
            StampWhere where = new StampWhere();
            where.leftColumn = new StampColumn(table, field);
            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
        }
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        this.gammars.add("column");
        if (this.previous("operator")) {
            this.lastWhere.whereType = KeyWhereType.NORMAL;
            this.lastWhere.rightColumn = new StampColumn(field);
        } else {
            StampWhere where = new StampWhere();
            where.leftColumn = new StampColumn(aliasName, field);
            if (this.lastWhere != null) this.lastWhere.next = where;
            this.lastWhere = where;
            if (this.where == null) this.where = where;
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
        this.lastWhere.nextLogic = KeyLogic.OR;
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
