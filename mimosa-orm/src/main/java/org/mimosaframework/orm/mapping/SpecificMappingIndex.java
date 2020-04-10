package org.mimosaframework.orm.mapping;

import java.util.List;

public class SpecificMappingIndex implements MappingIndex {
    private SpecificMappingTable mappingTable;

    /**
     * 数据库中的索引名称
     */
    private String databaseIndexName;

    public SpecificMappingTable getMappingTable() {
        return mappingTable;
    }

    public void setMappingTable(SpecificMappingTable mappingTable) {
        this.mappingTable = mappingTable;
    }

    public String getDatabaseIndexName() {
        return databaseIndexName;
    }

    public void setDatabaseIndexName(String databaseIndexName) {
        this.databaseIndexName = databaseIndexName;
    }

    @Override
    public String getIndexName() {
        return null;
    }

    @Override
    public List<MappingField> getIndexColumns() {
        return null;
    }

    @Override
    public IndexType getIndexType() {
        return null;
    }
}
