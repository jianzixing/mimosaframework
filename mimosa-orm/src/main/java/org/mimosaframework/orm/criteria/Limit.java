package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public class Limit {
    private long start;
    private long limit;

    public Limit() {
    }

    public Limit limit(long start, long count) {
        this.start = start;
        this.limit = count;
        return this;
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

    @Override
    public Limit clone() {
        Limit limit = new Limit();
        limit.start = this.start;
        limit.limit = this.limit;
        return limit;
    }
}
