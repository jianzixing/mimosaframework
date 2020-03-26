package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public class AbstractSQLSelectBuilder
        extends
        AbstractFunSQLBuilder

        implements
        RedefineSelectBuilder {

    protected boolean hasLeastOneSort = false;

    /**
     * sql 片段计数器
     */
    protected int columnCount = 0;
    protected int fieldCount = 0;

    @Override
    protected SQLBuilder createSQLBuilder() {
        return null;
    }

    @Override
    public MappingTable getMappingTableByClass(Class table) {
        return null;
    }

    @Override
    public Object select() {
        this.sqlBuilder.SELECT();
        this.body = 1;
        return this;
    }

    @Override
    public Object all() {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.addString("*");
        this.fieldCount++;
        return this;
    }

    @Override
    public Object field(Serializable... fields) {
        if (fields != null) {
            if (this.fieldCount > 0) this.sqlBuilder.addSplit();
            int i = 0;
            for (Serializable field : fields) {
                this.sqlBuilder.addMappingField(new SQLMappingField(field));
                i++;
                if (i != fields.length) this.sqlBuilder.addSplit();
                this.fieldCount++;
            }
        }
        return this;
    }

    @Override
    public Object field(Class table, Serializable... fields) {
        if (table != null && fields != null) {
            if (this.fieldCount > 0) this.sqlBuilder.addSplit();
            int i = 0;
            for (Serializable field : fields) {
                this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
                i++;
                if (i != fields.length) this.sqlBuilder.addSplit();
                this.fieldCount++;
            }
        }
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable... fields) {
        if (fields != null) {
            if (this.fieldCount > 0) this.sqlBuilder.addSplit();
            int i = 0;
            for (Serializable field : fields) {
                this.sqlBuilder.addMappingField(new SQLMappingField(tableAliasName, field));
                i++;
                if (i != fields.length) this.sqlBuilder.addSplit();
                this.fieldCount++;
            }
        }
        return this;
    }

    @Override
    public Object field(Serializable field, String fieldAliasName) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        this.fieldCount++;
        return this;
    }

    @Override
    public Object field(Class table, Serializable field, String fieldAliasName) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        this.fieldCount++;
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable field, String fieldAliasName) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingField(new SQLMappingField(tableAliasName, field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        this.fieldCount++;
        return this;
    }

    @Override
    public Object distinct(Serializable field) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.DISTINCT();
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        this.fieldCount++;
        return this;
    }

    @Override
    public Object distinct(String tableAliasName, Serializable field) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.DISTINCT();
        this.sqlBuilder.addMappingField(new SQLMappingField(tableAliasName, field));
        this.fieldCount++;
        return this;
    }

    @Override
    public Object distinct(Class table, Serializable field) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.DISTINCT();
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        this.fieldCount++;
        return this;
    }

    @Override
    public Object distinct(Serializable field, String fieldAliasName) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.DISTINCT();
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        this.fieldCount++;
        return this;
    }

    @Override
    public Object distinct(String tableAliasName, Serializable field, String fieldAliasName) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.DISTINCT();
        this.sqlBuilder.addMappingField(new SQLMappingField(tableAliasName, field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        this.fieldCount++;
        return this;
    }

    @Override
    public Object distinct(Class table, Serializable field, String fieldAliasName) {
        if (this.fieldCount > 0) this.sqlBuilder.addSplit();
        this.sqlBuilder.DISTINCT();
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        this.fieldCount++;
        return this;
    }

    @Override
    public Object table(Class table) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        return this;
    }

    @Override
    public Object value(Object value) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName, value);
        return this;
    }

    @Override
    public Object column(Serializable field) {
        if ((this.body == 12 || this.body == 13) && this.hasLeastOneSort && this.columnCount > 0) {
            this.sqlBuilder.addSplit();
        }
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        this.lastPlaceholderName = field.toString();

        this.columnCount++;
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        if ((this.body == 12 || this.body == 13) && this.hasLeastOneSort && this.columnCount > 0) {
            this.sqlBuilder.addSplit();
        }
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addTableWrapField(mappingTable.getMappingTableName(), field.toString());
        this.lastPlaceholderName = mappingTable.getMappingTableName() + "." + field.toString();

        this.columnCount++;
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        if ((this.body == 12 || this.body == 13) && this.hasLeastOneSort && this.columnCount > 0) {
            this.sqlBuilder.addSplit();
        }
        this.sqlBuilder.addMappingField(new SQLMappingField(aliasName, field));
        this.lastPlaceholderName = aliasName + "." + field.toString();

        this.columnCount++;
        return this;
    }

    @Override
    public Object and() {
        this.sqlBuilder.AND();
        return this;
    }

    @Override
    public Object from() {
        this.sqlBuilder.FROM();
        this.body = 2;
        return this;
    }

    @Override
    public Object having() {
        this.sqlBuilder.HAVING();
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.sqlBuilder.LIMIT().addString(pos + "," + len);
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        this.sqlBuilder.addString("ISNULL");
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addWrapString(field.toString());
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addString("ISNULL");
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addTableWrapField(mappingTable.getMappingTableName(), field.toString());
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        this.sqlBuilder.addString("ISNULL");
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addTableWrapField(aliasName, field.toString());
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        this.sqlBuilder.NOT();
        this.isNull(field);
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        this.sqlBuilder.NOT();
        this.isNull(table, field);
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        this.sqlBuilder.NOT();
        this.isNull(aliasName, field);
        return this;
    }

    @Override
    public Object in() {
        this.sqlBuilder.IN();
        return this;
    }

    @Override
    public Object nin() {
        this.sqlBuilder.NOT().IN();
        return this;
    }

    @Override
    public Object like() {
        this.sqlBuilder.LIKE();
        return this;
    }

    @Override
    public Object ne() {
        this.sqlBuilder.addString("!=");
        return this;
    }

    @Override
    public Object gt() {
        this.sqlBuilder.addString(">");
        return this;
    }

    @Override
    public Object gte() {
        this.sqlBuilder.addString(">=");
        return this;
    }

    @Override
    public Object lt() {
        this.sqlBuilder.addString("<");
        return this;
    }

    @Override
    public Object lte() {
        this.sqlBuilder.addString("<=");
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
        this.sqlBuilder.BETWEEN();
        return this;
    }

    @Override
    public BetweenValueBuilder notBetween() {
        this.sqlBuilder.NOT().BETWEEN();
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName + "&1", valueA);
        this.sqlBuilder.AND();
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName + "&2", valueB);
        return this;
    }

    @Override
    public Object eq() {
        this.sqlBuilder.addString("=");
        return this;
    }

    @Override
    public Object or() {
        this.sqlBuilder.OR();
        return this;
    }

    @Override
    public Object orderBy() {
        this.sqlBuilder.ORDER().BY();
        this.body = 12;
        this.hasLeastOneSort = true;
        this.columnCount = 0;
        return this;
    }

    @Override
    public Object asc() {
        this.sqlBuilder.ASC();
        return this;
    }

    @Override
    public Object desc() {
        this.sqlBuilder.DESC();
        return this;
    }

    @Override
    public Object where() {
        this.sqlBuilder.WHERE();
        this.body = 11;
        return this;
    }

    @Override
    public Object wrapper(AboutChildBuilder builder) {
        SQLBuilder sqlBuilder = builder.getSqlBuilder();
        sqlBuilder.setTableFieldReplaceRule(this.sqlBuilder.getRuleStart(), this.sqlBuilder.getRuleFinish());
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addSQLBuilder(sqlBuilder);
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object inner() {
        this.sqlBuilder.INNER();
        this.body = 4;
        return this;
    }

    @Override
    public Object join() {
        this.sqlBuilder.JOIN();
        return this;
    }

    @Override
    public Object left() {
        this.sqlBuilder.LEFT();
        this.body = 3;
        return this;
    }

    @Override
    public Object on() {
        this.sqlBuilder.ON();
        this.body = 10;
        return this;
    }

    @Override
    public Object groupBy() {
        this.sqlBuilder.GROUP().BY();
        this.body = 13;
        this.hasLeastOneSort = true;
        this.columnCount = 0;
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table, tableAliasName));
        this.sqlBuilder.AS().addWrapString(tableAliasName);
        return this;
    }
}
