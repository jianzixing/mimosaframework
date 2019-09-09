package org.mimosaframework.orm.platform;

/**
 * @author yangankang
 */
public class SQLSymbol {
    private SQLBuilder sqlBuilder;
    private Symbol symbol;

    public static SQLSymbol getInstance(SQLBuilder sqlBuilder, Symbol symbol) {
        return new SQLSymbol(sqlBuilder, symbol);
    }

    public SQLSymbol(SQLBuilder sqlBuilder, Symbol symbol) {
        this.sqlBuilder = sqlBuilder;
        this.symbol = symbol;
    }

    public SQLBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public void setSqlBuilder(SQLBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public enum Symbol {
        Brace, Parenthesis
    }
}
