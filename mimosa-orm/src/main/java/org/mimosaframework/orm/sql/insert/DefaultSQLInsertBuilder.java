package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampInsert;
import org.mimosaframework.orm.sql.stamp.StampSelect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLInsertBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineInsertBuilder {

    protected StampInsert stampInsert = new StampInsert();
    protected List<Object[]> values = new ArrayList<>();

    @Override
    public DefaultSQLInsertBuilder insert() {
        this.gammars.add("insert");
        return this;
    }

    @Override
    public DefaultSQLInsertBuilder columns(Serializable... fields) {
        this.gammars.add("columns");
        StampColumn[] columns = new StampColumn[fields.length];
        int i = 0;
        for (Serializable field : fields) {
            columns[i] = new StampColumn(field);
            i++;
        }
        this.stampInsert.columns = columns;
        return this;
    }

    @Override
    public DefaultSQLInsertBuilder table(Class table) {
        this.gammars.add("table");
        stampInsert.tableClass = table;
        return this;
    }

    @Override
    public DefaultSQLInsertBuilder into() {
        this.gammars.add("into");
        return this;
    }

    @Override
    public DefaultSQLInsertBuilder values() {
        this.gammars.add("values");
        return this;
    }

    @Override
    public DefaultSQLInsertBuilder row(Object... values) {
        this.gammars.add("row");
        this.values.add(values);
        return this;
    }

    @Override
    public StampInsert compile() {
        this.stampInsert.values = values.toArray(new Object[][]{});
        return this.stampInsert;
    }

    @Override
    public DefaultSQLInsertBuilder table(String name) {
        this.gammars.add("table");
        stampInsert.tableName = name;
        return this;
    }

    @Override
    public DefaultSQLInsertBuilder select(UnifyBuilder builder) {
        this.gammars.add("select");
        stampInsert.select = (StampSelect) builder.compile();
        return this;
    }
}
