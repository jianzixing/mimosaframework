package org.mimosaframework.orm.platform;

public class ExecuteImmediate {
    public String procedure;

    public String preview;
    public String sql;
    public String end;

    public ExecuteImmediate() {
    }

    public ExecuteImmediate(String sql) {
        this.sql = sql;
    }

    public ExecuteImmediate(StringBuilder sql) {
        if (sql != null) {
            this.sql = sql.toString();
        }
    }

    public ExecuteImmediate(String preview, String sql) {
        this.preview = preview;
        this.sql = sql;
    }

    public ExecuteImmediate(String preview, String sql, String end) {
        this.preview = preview;
        this.sql = sql;
        this.end = end;
    }

    public ExecuteImmediate setProcedure(String procedure) {
        this.procedure = procedure;
        return this;
    }
}
