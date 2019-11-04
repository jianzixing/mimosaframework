package org.mimosaframework.orm.sql;

public class HavingBuilder {
    private FunBuilder funBuilder;
    private SymbolType symbol;
    private Object value;

    public HavingBuilder(FunBuilder funBuilder, SymbolType symbol, Object value) {
        this.funBuilder = funBuilder;
        this.symbol = symbol;
        this.value = value;
    }

    public FunBuilder getFunBuilder() {
        return funBuilder;
    }

    public SymbolType getSymbol() {
        return symbol;
    }

    public Object getValue() {
        return value;
    }

    public static HavingBuilder builder(FunBuilder funBuilder, SymbolType symbol, Object value) {
        return new HavingBuilder(funBuilder, symbol, value);
    }
}
