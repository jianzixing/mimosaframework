package org.mimosaframework.orm.sql;

public class LimitBuilder {
    private long start;
    private long limit;

    public LimitBuilder(long start, long limit) {
        this.start = start;
        this.limit = limit;
    }

    public long getStart() {
        return start;
    }

    public long getLimit() {
        return limit;
    }
}
