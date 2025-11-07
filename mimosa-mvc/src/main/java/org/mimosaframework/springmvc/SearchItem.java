package org.mimosaframework.springmvc;

public class SearchItem {
    private String name;
    private String value;
    private Symbol symbol;
    private String start;
    private String end;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public enum Symbol {
        eq, between, gt, lt, gte, lte, like
    }

    public enum Logic {
        or, and
    }
}
