package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampInsert;

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
    public Object insert() {
        this.gammars.add("insert");
        return this;
    }

    @Override
    public Object columns(Serializable... fields) {
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
    public Object table(Class table) {
        this.gammars.add("table");
        stampInsert.table = table;
        return this;
    }

    @Override
    public Object into() {
        this.gammars.add("into");
        return this;
    }

    @Override
    public Object values() {
        this.gammars.add("values");
        return this;
    }

    @Override
    public Object row(Object... values) {
        this.gammars.add("row");
        this.values.add(values);
        return this;
    }

    @Override
    public StampAction compile() {
        this.stampInsert.values = values.toArray(new Object[][]{});
        return this.stampInsert;
    }
}
