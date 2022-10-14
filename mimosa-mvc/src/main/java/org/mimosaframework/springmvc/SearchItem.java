package org.mimosaframework.springmvc;

public class SearchItem {
    private String name;
    private String value;
    private Symbol symbol;
    private String start;
    private String end;
    // 和下一个条件的关系
    private Logic logic;

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

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    enum Symbol {
        eq, between, gt, lt, gte, lte, like
    }

    enum Logic {
        or, and
    }
}
