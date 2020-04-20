package org.mimosaframework.orm.sql.rename;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.KeyRenameType;
import org.mimosaframework.orm.sql.stamp.StampRename;

import java.io.Serializable;

public class DefaultSQLRenameBuilder
        extends
        AbstractSQLBuilder
        implements RedefineRenameBuilder {

    private StampRename rename = new StampRename();

    @Override
    public DefaultSQLRenameBuilder name(String value) {
        this.gammars.add("name");
        rename.newName = value;
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder table(Class table) {
        if (!this.previous("on")) {
            this.addPoint("table");
            rename.tableClass = table;
            rename.renameType = KeyRenameType.TABLE;
        } else {
            this.gammars.add("table");
            rename.tableClass = table;
        }
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder column() {
        this.addPoint("column");
        rename.renameType = KeyRenameType.COLUMN;
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder index() {
        this.addPoint("index");
        rename.renameType = KeyRenameType.INDEX;
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder on() {
        this.gammars.add("on");
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder rename() {
        this.gammars.add("rename");
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder to() {
        this.gammars.add("to");
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder table(String name) {
        if (!this.previous("on")) {
            this.addPoint("table");
            rename.tableName = name;
            rename.renameType = KeyRenameType.TABLE;
        } else {
            this.gammars.add("table");
            rename.tableName = name;
        }
        return this;
    }

    @Override
    public StampRename compile() {
        return rename;
    }

    @Override
    public DefaultSQLRenameBuilder newColumn(String field) {
        this.gammars.add("newColumn");
        rename.newName = field;
        return this;
    }

    @Override
    public DefaultSQLRenameBuilder oldColumn(String field) {
        this.gammars.add("oldColumn");
        rename.oldName = field;
        return this;
    }
}
