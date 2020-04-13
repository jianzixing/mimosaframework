package org.mimosaframework.orm.criteria;

public class WrapsObject<T> {
    private T where;
    private Wraps link;
    private CriteriaLogic logic = CriteriaLogic.AND;

    public WrapsObject() {
    }

    public WrapsObject(Wraps link) {
        this.link = link;
    }

    public WrapsObject(T where) {
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

    public Wraps getLink() {
        return link;
    }

    public void setLink(Wraps link) {
        this.link = link;
    }

    public void setWhere(T where) {
        this.where = where;
    }
}
