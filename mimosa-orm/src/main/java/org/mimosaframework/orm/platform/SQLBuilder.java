package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author yangankang
 */
public interface SQLBuilder {

    SQLBuilder setTableFieldReplaceRule(String ruleStart, String ruleFinish);

    SQLBuilder setMapValue(Map map);

    Map getMapValue();

    SQLBuilder addParenthesisStart();

    SQLBuilder addParenthesisEnd();

    SQLBuilder addSQLBuilder(SQLBuilder ws);

    SQLBuilder addSQLString(String sql);

    SQLBuilder addAsterisk();

    SQLBuilder addTableField(String tableAliasName, String fieldName);

    SQLBuilder addTableWrapField(String tableAliasName, String fieldName);

    SQLBuilder addFun(String funName, Object field, String alias);

    SQLBuilder addValueFun(String funName, Object field, String alias);

    SQLBuilder addFun(String funName, String tableAliasName, Object field, String alias);

    SQLBuilder INNER();

    SQLBuilder FULL();

    SQLBuilder COLUMN();

    SQLBuilder HAVING();

    void removeLast();

    String getRuleStart();

    String getRuleFinish();

    enum Command {
        CREATE, TABLE, IF, NOT, EXISTS, PRIMARY, KEY, AUTO_INCREMENT, NULL, DEFAULT,
        INDEX, ALTER, ADD, INSERT, INTO, VALUES, UPDATE, SET, WHERE, AND, OR, ENGINE,
        CHARSET, IN, LIKE, BY, ORDER, ASC, DESC, LIMIT, DELETE, FROM, SELECT, COUNT,
        ON, AS, LEFT, JOIN, FIRST, AFTER, COMMENT, SUM, MODIFY, DROP, IS, INNER, FULL,
        COLUMN, UNIQUE, GROUP, BETWEEN, ALL, CHARACTER, COLLATE, OFF, HAVING, DATABASE,
        TO, CHANGE, RENAME, FULLTEXT,
        CONSTRAINT
    }

    SQLBuilder TO();

    SQLBuilder OFF();

    SQLBuilder CHARACTER();

    SQLBuilder COLLATE();

    SQLBuilder ALL();

    SQLBuilder BETWEEN();

    SQLBuilder GROUP();

    SQLBuilder CONSTRAINT();

    SQLBuilder addEqualMark();

    SQLBuilder addEndMark();

    SQLBuilder CREATE();

    SQLBuilder TABLE();

    SQLBuilder TABLE(String tableName);

    SQLBuilder DATABASE();

    SQLBuilder IF();

    SQLBuilder IS();

    SQLBuilder MODIFY();

    SQLBuilder RENAME();

    SQLBuilder CHANGE();

    SQLBuilder DROP();

    SQLBuilder NOT();

    SQLBuilder EXISTS();

    SQLBuilder PRIMARY();

    SQLBuilder KEY();

    SQLBuilder AUTO_INCREMENT();

    SQLBuilder NULL();

    SQLBuilder DEFAULT();

    SQLBuilder INDEX();

    SQLBuilder ALTER();

    SQLBuilder ADD();

    SQLBuilder UNIQUE();

    SQLBuilder FULLTEXT();

    SQLBuilder INSERT();

    SQLBuilder INTO();

    SQLBuilder VALUES();

    SQLBuilder UPDATE();

    SQLBuilder SET();

    SQLBuilder WHERE();

    SQLBuilder AND();

    SQLBuilder OR();

    SQLBuilder ENGINE();

    SQLBuilder CHARSET();

    SQLBuilder IN();

    SQLBuilder LIKE();

    SQLBuilder BY();

    SQLBuilder ORDER();

    SQLBuilder ASC();

    SQLBuilder DESC();

    SQLBuilder LIMIT();

    SQLBuilder DELETE();

    SQLBuilder FROM();

    SQLBuilder SELECT();

    SQLBuilder COUNT();

    SQLBuilder ON();

    SQLBuilder AS();

    SQLBuilder LEFT();

    SQLBuilder JOIN();

    SQLBuilder FIRST();

    SQLBuilder AFTER();

    SQLBuilder COMMENT();

    SQLBuilder SUM();

    /**
     * {}
     *
     * @param sqlBuilder
     */
    SQLBuilder symbolBrace(SQLBuilder sqlBuilder);

    /**
     * ()
     *
     * @param sqlBuilder
     */
    SQLBuilder symbolParenthesis(SQLBuilder sqlBuilder);

    SQLBuilder field(SQLField field);

    /**
     * ?
     */
    SQLBuilder questionMark();

    SQLBuilder addString(String name);

    SQLBuilder addWrapString(String name);

    SQLBuilder addParenthesisString(String name);

    SQLBuilder addParenthesisWrapString(String... name);

    SQLBuilder addQuotesString(String name);

    SQLBuilder addSplit();

    SQLBuilder addDataPlaceholder(String name, Object value);

    SQLBuilder addDataPlaceholder(SQLDataPlaceholder placeholder);

    List<SQLDataPlaceholder> getDataPlaceholders();

    SQLBuilderCombine toSQLString();

    SQLBuilderCombine toSQLString(MappingGlobalWrapper mappingGlobalWrapper);

    SQLBuilderCombine toSQLString(MappingGlobalWrapper mappingGlobalWrapper,
                                  List<SQLMappingTable> fromTables);

    SQLBuilder addMappingTable(SQLMappingTable mappingTable);

    SQLBuilder addMappingField(SQLMappingField mappingField);

    int size();
}
