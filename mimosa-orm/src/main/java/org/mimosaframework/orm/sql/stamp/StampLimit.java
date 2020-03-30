package org.mimosaframework.orm.sql.stamp;

public class StampLimit {
    public int start;
    public int limit;

    public StampLimit(int start, int limit) {
        this.start = start;
        this.limit = limit;
    }
}
