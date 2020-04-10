package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.sql.stamp.KeyColumnType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlatformDialect {
    private Map<KeyColumnType, ColumnType> columnTypes = new HashMap<>();

    protected void registerColumnType(KeyColumnType type, String typeName) {
        this.columnTypes.put(type, new ColumnType(type, typeName, -1, -1));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length) {
        this.columnTypes.put(type, new ColumnType(type, typeName, length, -1));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length, int scale) {
        this.columnTypes.put(type, new ColumnType(type, typeName, length, scale));
    }

    public List<TableStructure> getTableStructures() {
        return null;
    }

    public ColumnType getColumnType(KeyColumnType type) {
        return columnTypes.get(type);
    }
}
