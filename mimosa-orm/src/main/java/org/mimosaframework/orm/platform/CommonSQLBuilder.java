package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.DefaultSession;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author yangankang
 */
public class CommonSQLBuilder implements SQLBuilder {
    private List sql = new ArrayList();
    private String ruleStart = "`"; // 默认是mysql的
    private String ruleFinish = "`"; // 默认是mysql的
    private Map map;

    public SQLBuilder setTableFieldReplaceRule(String ruleStart, String ruleFinish) {
        this.ruleStart = ruleStart;
        this.ruleFinish = ruleFinish;
        return this;
    }

    @Override
    public SQLBuilder setMapValue(Map map) {
        this.map = map;
        return this;
    }

    @Override
    public Map getMapValue() {
        return this.map;
    }

    @Override
    public SQLBuilder addParenthesisStart() {
        sql.add("(");
        return this;
    }

    @Override
    public SQLBuilder addParenthesisEnd() {
        sql.add(")");
        return this;
    }

    @Override
    public SQLBuilder addSQLBuilder(SQLBuilder ws) {
        if (ws != null) {
            sql.add(ws);
        }
        return this;
    }

    @Override
    public SQLBuilder addSQLString(String sql) {
        if (StringTools.isNotEmpty(sql)) {
            this.sql.add(sql);
        }
        return this;
    }

    @Override
    public SQLBuilder addAsterisk() {
        sql.add("*");
        return this;
    }

    @Override
    public SQLBuilder addTableField(String tableAliasName, String fieldName) {
        if (fieldName.equals("*")) {
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, tableAliasName, SQLContinuous.RuleType.RULE_FINISH, ".", fieldName));
        } else if (StringTools.isNotEmpty(tableAliasName)) {
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, tableAliasName, SQLContinuous.RuleType.RULE_FINISH, ".",
                    SQLContinuous.RuleType.RULE_START, fieldName, SQLContinuous.RuleType.RULE_FINISH));
        } else {
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, fieldName, SQLContinuous.RuleType.RULE_FINISH));
        }
        return this;
    }

    @Override
    public SQLBuilder addTableWrapField(String tableAliasName, String fieldName) {
        if (fieldName.equals("*")) {
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, tableAliasName, SQLContinuous.RuleType.RULE_FINISH, ".", fieldName));
        } else if (StringTools.isNotEmpty(tableAliasName)) {
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, tableAliasName, SQLContinuous.RuleType.RULE_FINISH,
                    ".", SQLContinuous.RuleType.RULE_START, fieldName, SQLContinuous.RuleType.RULE_FINISH));
        } else {
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, fieldName, SQLContinuous.RuleType.RULE_FINISH));
        }
        return this;
    }

    @Override
    public SQLBuilder addFun(String funName, Object field, String alias) {
        sql.add(new SQLFunction(funName, field, alias));
        return this;
    }

    @Override
    public SQLBuilder addValueFun(String funName, Object field, String alias) {
        sql.add(new SQLFunction(funName, field, alias, true));
        return this;
    }

    @Override
    public SQLBuilder addFun(String funName, String tableAliasName, Object field, String alias) {
        sql.add(new SQLFunction(funName, tableAliasName, field, alias));
        return this;
    }

    @Override
    public SQLBuilder INNER() {
        sql.add(Command.INNER);
        return this;
    }

    @Override
    public SQLBuilder FULL() {
        sql.add(Command.FULL);
        return this;
    }

    @Override
    public SQLBuilder COLUMN() {
        sql.add(Command.COLUMN);
        return this;
    }

    @Override
    public SQLBuilder HAVING() {
        sql.add(Command.HAVING);
        return this;
    }

    @Override
    public SQLBuilder USING() {
        sql.add(Command.USING);
        return this;
    }

    @Override
    public SQLBuilder DISTINCT() {
        sql.add(Command.DISTINCT);
        return this;
    }

    @Override
    public void removeLast() {
        sql.remove(sql.size() - 1);
    }

    @Override
    public String getRuleStart() {
        return this.ruleStart;
    }

    @Override
    public String getRuleFinish() {
        return this.ruleFinish;
    }

    @Override
    public SQLBuilder TO() {
        sql.add(Command.TO);
        return this;
    }

    @Override
    public SQLBuilder OFF() {
        sql.add(Command.OFF);
        return this;
    }

    @Override
    public SQLBuilder CHARACTER() {
        sql.add(Command.CHARACTER);
        return this;
    }

    @Override
    public SQLBuilder COLLATE() {
        sql.add(Command.COLLATE);
        return this;
    }

    @Override
    public SQLBuilder ALL() {
        sql.add(Command.ALL);
        return this;
    }

    @Override
    public SQLBuilder BETWEEN() {
        sql.add(Command.BETWEEN);
        return this;
    }

    @Override
    public SQLBuilder GROUP() {
        sql.add(Command.GROUP);
        return this;
    }

    @Override
    public SQLBuilder CONSTRAINT() {
        sql.add(Command.CONSTRAINT);
        return this;
    }

    @Override
    public SQLBuilder addEqualMark() {
        sql.add("=");
        return this;
    }

    @Override
    public SQLBuilder addEndMark() {
        sql.add(";");
        return this;
    }

    @Override
    public SQLBuilder CREATE() {
        sql.add(Command.CREATE);
        return this;
    }

    @Override
    public SQLBuilder TABLE() {
        sql.add(Command.TABLE);
        return this;
    }

    @Override
    public SQLBuilder TABLE(String tableName) {
        sql.add(Command.TABLE);
        if (tableName != null)
            sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, tableName, SQLContinuous.RuleType.RULE_FINISH));
        return this;
    }

    @Override
    public SQLBuilder DATABASE() {
        sql.add(Command.DATABASE);
        return this;
    }

    @Override
    public SQLBuilder IF() {
        sql.add(Command.IF);
        return this;
    }

    @Override
    public SQLBuilder IS() {
        sql.add(Command.IS);
        return this;
    }

    @Override
    public SQLBuilder MODIFY() {
        sql.add(Command.MODIFY);
        return this;
    }

    @Override
    public SQLBuilder RENAME() {
        sql.add(Command.RENAME);
        return this;
    }

    @Override
    public SQLBuilder CHANGE() {
        sql.add(Command.CHANGE);
        return this;
    }

    @Override
    public SQLBuilder DROP() {
        sql.add(Command.DROP);
        return this;
    }

    @Override
    public SQLBuilder NOT() {
        sql.add(Command.NOT);
        return this;
    }

    @Override
    public SQLBuilder EXISTS() {
        sql.add(Command.EXISTS);
        return this;
    }

    @Override
    public SQLBuilder PRIMARY() {
        sql.add(Command.PRIMARY);
        return this;
    }

    @Override
    public SQLBuilder KEY() {
        sql.add(Command.KEY);
        return this;
    }

    @Override
    public SQLBuilder AUTO_INCREMENT() {
        sql.add(Command.AUTO_INCREMENT);
        return this;
    }

    @Override
    public SQLBuilder NULL() {
        sql.add(Command.NULL);
        return this;
    }

    @Override
    public SQLBuilder DEFAULT() {
        sql.add(Command.DEFAULT);
        return this;
    }

    @Override
    public SQLBuilder INDEX() {
        sql.add(Command.INDEX);
        return this;
    }

    @Override
    public SQLBuilder ALTER() {
        sql.add(Command.ALTER);
        return this;
    }

    @Override
    public SQLBuilder ADD() {
        sql.add(Command.ADD);
        return this;
    }

    @Override
    public SQLBuilder UNIQUE() {
        sql.add(Command.UNIQUE);
        return this;
    }

    @Override
    public SQLBuilder FULLTEXT() {
        sql.add(Command.FULLTEXT);
        return this;
    }

    @Override
    public SQLBuilder INSERT() {
        sql.add(Command.INSERT);
        return this;
    }

    @Override
    public SQLBuilder INTO() {
        sql.add(Command.INTO);
        return this;
    }

    @Override
    public SQLBuilder VALUES() {
        sql.add(Command.VALUES);
        return this;
    }

    @Override
    public SQLBuilder UPDATE() {
        sql.add(Command.UPDATE);
        return this;
    }

    @Override
    public SQLBuilder SET() {
        sql.add(Command.SET);
        return this;
    }

    @Override
    public SQLBuilder WHERE() {
        sql.add(Command.WHERE);
        return this;
    }

    @Override
    public SQLBuilder AND() {
        sql.add(Command.AND);
        return this;
    }

    @Override
    public SQLBuilder OR() {
        sql.add(Command.OR);
        return this;
    }

    @Override
    public SQLBuilder ENGINE() {
        sql.add(Command.ENGINE);
        return this;
    }

    @Override
    public SQLBuilder CHARSET() {
        sql.add(Command.CHARSET);
        return this;
    }

    @Override
    public SQLBuilder IN() {
        sql.add(Command.IN);
        return this;
    }

    @Override
    public SQLBuilder LIKE() {
        sql.add(Command.LIKE);
        return this;
    }

    @Override
    public SQLBuilder BY() {
        sql.add(Command.BY);
        return this;
    }

    @Override
    public SQLBuilder ORDER() {
        sql.add(Command.ORDER);
        return this;
    }

    @Override
    public SQLBuilder ASC() {
        sql.add(Command.ASC);
        return this;
    }

    @Override
    public SQLBuilder DESC() {
        sql.add(Command.DESC);
        return this;
    }

    @Override
    public SQLBuilder LIMIT() {
        sql.add(Command.LIMIT);
        return this;
    }

    @Override
    public SQLBuilder DELETE() {
        sql.add(Command.DELETE);
        return this;
    }

    @Override
    public SQLBuilder FROM() {
        sql.add(Command.FROM);
        return this;
    }

    @Override
    public SQLBuilder SELECT() {
        sql.add(Command.SELECT);
        return this;
    }

    @Override
    public SQLBuilder COUNT() {
        sql.add(Command.COUNT);
        return this;
    }

    @Override
    public SQLBuilder ON() {
        sql.add(Command.ON);
        return this;
    }

    @Override
    public SQLBuilder AS() {
        sql.add(Command.AS);
        return this;
    }

    @Override
    public SQLBuilder LEFT() {
        sql.add(Command.LEFT);
        return this;
    }

    @Override
    public SQLBuilder JOIN() {
        sql.add(Command.JOIN);
        return this;
    }

    @Override
    public SQLBuilder FIRST() {
        sql.add(Command.FIRST);
        return this;
    }

    @Override
    public SQLBuilder AFTER() {
        sql.add(Command.AFTER);
        return this;
    }

    @Override
    public SQLBuilder COMMENT() {
        sql.add(Command.COMMENT);
        return this;
    }

    @Override
    public SQLBuilder SUM() {
        sql.add(Command.SUM);
        return this;
    }

    @Override
    public SQLBuilder symbolBrace(SQLBuilder sqlBuilder) {
        sql.add(SQLSymbol.getInstance(sqlBuilder, SQLSymbol.Symbol.Brace));
        return this;
    }

    @Override
    public SQLBuilder symbolParenthesis(SQLBuilder sqlBuilder) {
        sql.add(SQLSymbol.getInstance(sqlBuilder, SQLSymbol.Symbol.Parenthesis));
        return this;
    }

    @Override
    public SQLBuilder field(SQLField field) {
        sql.add(field);
        return this;
    }

    @Override
    public SQLBuilder questionMark() {
        sql.add("?");
        return this;
    }

    @Override
    public SQLBuilder addString(String name) {
        sql.add(name);
        return this;
    }

    @Override
    public SQLBuilder addWrapString(String name) {
        sql.add(new SQLContinuous(SQLContinuous.RuleType.RULE_START, name, SQLContinuous.RuleType.RULE_FINISH));
        return this;
    }

    @Override
    public SQLBuilder addParenthesisString(String name) {
        sql.add("(" + name + ")");
        return this;
    }

    @Override
    public SQLBuilder addParenthesisWrapString(String... name) {
        if (name != null && name.length > 0) {
            sql.add("(");
            for (String s : name) {
                this.addWrapString(s);
                sql.add(",");
            }
            sql.remove(sql.size() - 1);
            sql.add(")");
        }
        return this;
    }

    @Override
    public SQLBuilder addQuotesString(String name) {
        sql.add("'" + name + "'");
        return this;
    }

    @Override
    public SQLBuilder addSplit() {
        sql.add(",");
        return this;
    }

    public SQLBuilderCombine toSQLString() {
        return this.toSQLString(null);
    }

    public SQLBuilderCombine toSQLString(MappingGlobalWrapper mappingGlobalWrapper) {
        return this.toSQLString(mappingGlobalWrapper, null);
    }

    public SQLBuilderCombine toSQLString(MappingGlobalWrapper mappingGlobalWrapper,
                                         List<SQLMappingTable> fromTables) {
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<SQLMappingTable> sqlMappingTables = fromTables;
        for (Object o : sql) {
            if (o instanceof SQLMappingTable) {
                if (sqlMappingTables == null) {
                    sqlMappingTables = new ArrayList<>();
                }
                sqlMappingTables.add((SQLMappingTable) o);
            }
        }
        for (Object o : sql) {
            this.toSQLString(sb, o, placeholders, mappingGlobalWrapper, sqlMappingTables);
        }
        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    private void toSQLString(StringBuilder sb,
                             Object o,
                             List<SQLDataPlaceholder> placeholders,
                             MappingGlobalWrapper mappingGlobalWrapper,
                             List<SQLMappingTable> sqlMappingTables) {
        if (o instanceof SQLContinuous) {
            sb.append(((SQLContinuous) o).toString(ruleStart, ruleFinish));
        } else if (o instanceof Command) {
            sb.append(((Command) o).name());
        } else if (o instanceof SQLField) {
            sb.append(o.toString());
        } else if (o instanceof SQLMappingTable) {
            String tableName = ((SQLMappingTable) o).getDatabaseTableName(mappingGlobalWrapper);
            if (StringTools.isEmpty(tableName)) {
                throw new IllegalArgumentException(
                        Messages.get(LanguageMessageFactory.PROJECT,
                                CommonSQLBuilder.class, "miss_table_mapping",
                                ((SQLMappingTable) o).getTable().getName())
                );
            }
            sb.append(tableName);
        } else if (o instanceof SQLMappingField) {
            String tableAliasName = ((SQLMappingField) o).getTableAliasName();
            String field = ((SQLMappingField) o).getMayBeField(sqlMappingTables, mappingGlobalWrapper);

            if (StringTools.isNotEmpty(tableAliasName)) {
                sb.append(ruleStart + tableAliasName + ruleFinish);
                sb.append(".");
                sb.append(ruleStart + field + ruleFinish);
            } else {
                sb.append(ruleStart + field + ruleFinish);
            }
        } else if (o instanceof SQLReplaceHolder) {
            this.toSQLString(sb, ((SQLReplaceHolder) o).getHolder(), placeholders, mappingGlobalWrapper, sqlMappingTables);
        } else if (o instanceof SQLSymbol) {
            SQLSymbol.Symbol symbol = ((SQLSymbol) o).getSymbol();
            SQLBuilderCombine combine = ((SQLSymbol) o).getSqlBuilder().toSQLString();
            if (symbol == SQLSymbol.Symbol.Parenthesis) {
                sb.append("(");
                sb.append(combine.getSql());
                sb.append(")");
            } else if (symbol == SQLSymbol.Symbol.Brace) {
                sb.append("{");
                sb.append(combine.getSql());
                sb.append("}");
            }
            if (combine.getPlaceholders() != null) {
                placeholders.addAll(combine.getPlaceholders());
            }
        } else if (o instanceof String) {
            sb.append(String.valueOf(o));
        } else if (o instanceof SQLBuilder) {
            SQLBuilderCombine combine = ((SQLBuilder) o).toSQLString(mappingGlobalWrapper, sqlMappingTables);
            sb.append(combine.getSql().trim());
            if (combine.getPlaceholders() != null) {
                placeholders.addAll(combine.getPlaceholders());
            }
        } else if (o instanceof SQLFunction) {
            sb.append(((SQLFunction) o).getFunName());
            String tableAliasName = ((SQLFunction) o).getTableAliasName();
            Object f = ((SQLFunction) o).getField();
            boolean isValue = ((SQLFunction) o).isValue();
            if (isValue) {
                sb.append("(" + f + ")");
            } else {
                if (f == null) {
                    sb.append("(*)");
                } else if (f instanceof Integer) {
                    sb.append("(" + f + ")");
                } else {
                    if (StringTools.isNotEmpty(tableAliasName)) {
                        sb.append("(" + ruleStart + tableAliasName + ruleFinish + "." + ruleStart + f + ruleFinish + ")");
                    } else {
                        sb.append("(" + ruleStart);
                        sb.append(f);
                        sb.append(ruleFinish + ")");
                    }
                }
            }

            String alias = ((SQLFunction) o).getFieldAliasName();
            if (alias != null) {
                sb.append(" ");
                sb.append("AS ");
                sb.append(ruleStart + alias + ruleFinish);
            }
            sb.append(" ");
        } else if (o instanceof SQLDataPlaceholder) {
            sb.append("?");
            placeholders.add((SQLDataPlaceholder) o);
        } else {
            sb.append(o);
        }

        if (o instanceof SQLFunction) {

        } else {
            sb.append(" ");
        }
    }

    @Override
    public SQLBuilder addMappingTable(SQLMappingTable mappingTable) {
        sql.add(mappingTable);
        return this;
    }

    @Override
    public SQLBuilder addMappingField(SQLMappingField mappingField) {
        sql.add(mappingField);
        return this;
    }

    @Override
    public int size() {
        return sql.size();
    }

    @Override
    public SQLBuilder addDataPlaceholder(String name, Object value) {
        SQLDataPlaceholder placeholder = new SQLDataPlaceholder(name, value);
        sql.add(placeholder);
        return this;
    }

    @Override
    public SQLBuilder addDataPlaceholder(SQLDataPlaceholder placeholder) {
        if (placeholder != null) {
            sql.add(placeholder);
        }
        return this;
    }

    @Override
    public List<SQLDataPlaceholder> getDataPlaceholders() {
        List<SQLDataPlaceholder> placeholders = null;
        for (Object o : sql) {
            if (o instanceof SQLBuilder) {
                SQLBuilderCombine combine = ((SQLBuilder) o).toSQLString();
                if (combine.getPlaceholders() != null) {
                    if (placeholders == null) {
                        placeholders = new LinkedList<>();
                    }
                    placeholders.addAll(combine.getPlaceholders());
                }
            }
            if (o instanceof SQLDataPlaceholder) {
                if (placeholders == null) {
                    placeholders = new LinkedList<>();
                }
                placeholders.add((SQLDataPlaceholder) o);
            }
        }
        return placeholders;
    }
}
