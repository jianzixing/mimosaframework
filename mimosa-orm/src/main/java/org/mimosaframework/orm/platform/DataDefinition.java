package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;

public class DataDefinition {
    // for create
    private MappingTable mappingTable;
    // for delete
    private TableStructure tableStructure;

    // for modify or create
    private MappingField mappingField;
    // for modify or delete
    private TableColumnStructure columnStructure;

    // for create
    private MappingIndex mappingIndex;
    // for delete
    private TableIndexStructure indexStructure;

    private DataDefinitionType type;

    public DataDefinition() {
    }

    public DataDefinition(DataDefinitionType type, MappingTable mappingTable) {
        this.type = type;
        this.mappingTable = mappingTable;
    }

    public DataDefinition(DataDefinitionType type, TableStructure tableStructure) {
        this.type = type;
        this.tableStructure = tableStructure;
    }

    public DataDefinition(DataDefinitionType type,
                          MappingTable mappingTable,
                          TableStructure tableStructure,
                          MappingField mappingField) {
        this.type = type;
        this.tableStructure = tableStructure;
        this.mappingTable = mappingTable;
        this.mappingField = mappingField;
    }

    public DataDefinition(DataDefinitionType type, MappingTable mappingTable,
                          TableStructure tableStructure,
                          TableColumnStructure columnStructure) {
        this.type = type;
        this.mappingTable = mappingTable;
        this.tableStructure = tableStructure;
        this.columnStructure = columnStructure;
    }

    public DataDefinition(DataDefinitionType type, TableStructure tableStructure,
                          MappingTable mappingTable, MappingField mappingField,
                          TableColumnStructure columnStructure) {
        this.type = type;
        this.tableStructure = tableStructure;
        this.mappingTable = mappingTable;
        this.mappingField = mappingField;
        this.columnStructure = columnStructure;
    }

    public DataDefinition(DataDefinitionType type, MappingTable mappingTable, MappingIndex mappingIndex) {
        this.type = type;
        this.mappingTable = mappingTable;
        this.mappingIndex = mappingIndex;
    }

    public DataDefinition(DataDefinitionType type, MappingTable mappingTable, TableIndexStructure indexStructure) {
        this.type = type;
        this.mappingTable = mappingTable;
        this.indexStructure = indexStructure;
    }

    public MappingTable getMappingTable() {
        return mappingTable;
    }

    public void setMappingTable(MappingTable mappingTable) {
        this.mappingTable = mappingTable;
    }

    public MappingField getMappingField() {
        return mappingField;
    }

    public void setMappingField(MappingField mappingField) {
        this.mappingField = mappingField;
    }

    public TableColumnStructure getColumnStructure() {
        return columnStructure;
    }

    public void setColumnStructure(TableColumnStructure columnStructure) {
        this.columnStructure = columnStructure;
    }

    public DataDefinitionType getType() {
        return type;
    }

    public void setType(DataDefinitionType type) {
        this.type = type;
    }

    public TableStructure getTableStructure() {
        return tableStructure;
    }

    public void setTableStructure(TableStructure tableStructure) {
        this.tableStructure = tableStructure;
    }

    public MappingIndex getMappingIndex() {
        return mappingIndex;
    }

    public void setMappingIndex(MappingIndex mappingIndex) {
        this.mappingIndex = mappingIndex;
    }

    public TableIndexStructure getIndexStructure() {
        return indexStructure;
    }

    public void setIndexStructure(TableIndexStructure indexStructure) {
        this.indexStructure = indexStructure;
    }
}
