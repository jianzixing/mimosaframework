package org.mimosaframework.orm.sql.stamp;

public class StampLimit {
    public long start;
    public long limit;

    public StampLimit(long start, long limit) {
        this.start = start;
        this.limit = limit;
    }
}
