package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.SQLAutonomously;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.List;

public class StructureBuilder implements
        UnifyBuilder,
        TableBuilder<UnifyBuilder> {

    protected StampStructure structure = new StampStructure();

    @Override
    public StampAction compile() {
        return structure;
    }

    @Override
    public SQLAutonomously autonomously() {
        SQLAutonomously autonomously = SQLAutonomously.newInstance(this);
        return autonomously;
    }

    public UnifyBuilder column(List<String> tables) {
        structure.type = 1;
        structure.tables = tables;
        return this;
    }

    public UnifyBuilder column(String schema, List<String> tables) {
        structure.type = 1;
        structure.tables = tables;
        structure.schema = schema;
        return this;
    }

    @Override
    public UnifyBuilder table() {
        structure.type = 0;
        return this;
    }

    public UnifyBuilder table(String schema) {
        structure.type = 0;
        structure.schema = schema;
        return this;
    }

    public UnifyBuilder index(List<String> tables) {
        structure.type = 2;
        structure.tables = tables;
        return this;
    }

    public UnifyBuilder index(String schema, List<String> tables) {
        structure.type = 2;
        structure.schema = schema;
        structure.tables = tables;
        return this;
    }
}
