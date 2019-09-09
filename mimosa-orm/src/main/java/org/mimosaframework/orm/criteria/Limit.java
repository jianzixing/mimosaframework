package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public class Limit implements LimitInterface {

    private Query query;

    private long start;

    private long limit;

    public Limit() {
    }

    public Limit(Query query) {
        this.query = query;
    }

    public Query goQuery() {
        return this.query;
    }

    public long getStart() {
        return start;
    }

    public Limit setStart(long start) {
        this.start = start;
        return this;
    }

    public long getLimit() {
        return limit;
    }

    public Limit setLimit(long limit) {
        this.limit = limit;
        return this;
    }

    public Limit limit(long start, long count) {
        this.start = start;
        this.limit = count;
        return this;
    }
}
