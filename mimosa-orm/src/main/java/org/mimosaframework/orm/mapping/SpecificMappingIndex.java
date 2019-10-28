package org.mimosaframework.orm.mapping;

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
}
