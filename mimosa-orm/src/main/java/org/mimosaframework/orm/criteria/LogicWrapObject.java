package org.mimosaframework.orm.criteria;

public class LogicWrapObject<T> {
    private T where;
    private LogicWraps link;
    private CriteriaLogic logic = CriteriaLogic.AND;

    public LogicWrapObject() {
    }

    public LogicWrapObject(LogicWraps link) {
        this.link = link;
    }

    public LogicWrapObject(T where) {
        this.where = where;
    }

    public T getWhere() {
        return where;
    }

    public CriteriaLogic getLogic() {
        return logic;
    }

    public void setLogic(CriteriaLogic logic) {
        this.logic = logic;
    }

    public LogicWraps getLink() {
        return link;
    }

    public void setLink(LogicWraps link) {
        this.link = link;
    }

    public void setWhere(T where) {
        this.where = where;
    }
}
