package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.List;
import java.util.Map;

public class CompareUpdateTableMate {
    private List<TableStructure> tableStructures;

    private MappingTable mappingTable;
    private TableStructure structure;

    private MappingTable createTable;
    private Map<MappingField, CompareUpdateMate> updateFields = null;
    private List<MappingField> createFields = null;
    private List<TableColumnStructure> delColumns = null;
    private List<MappingIndex> updateIndexes = null;
    private List<MappingIndex> newIndexes = null;
    private List<String> dropIndexes = null;

    public List<TableStructure> getTableStructures() {
        return tableStructures;
    }

    public void setTableStructures(List<TableStructure> tableStructures) {
        this.tableStructures = tableStructures;
    }

    public MappingTable getMappingTable() {
        return mappingTable;
    }

    public void setMappingTable(MappingTable mappingTable) {
        this.mappingTable = mappingTable;
    }

    public TableStructure getStructure() {
        return structure;
    }

    public void setStructure(TableStructure structure) {
        this.structure = structure;
    }

    public MappingTable getCreateTable() {
        return createTable;
    }

    public void setCreateTable(MappingTable createTable) {
        this.createTable = createTable;
    }

    public Map<MappingField, CompareUpdateMate> getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(Map<MappingField, CompareUpdateMate> updateFields) {
        this.updateFields = updateFields;
    }

    public List<MappingField> getCreateFields() {
        return createFields;
    }

    public void setCreateFields(List<MappingField> createFields) {
        this.createFields = createFields;
    }

    public List<TableColumnStructure> getDelColumns() {
        return delColumns;
    }

    public void setDelColumns(List<TableColumnStructure> delColumns) {
        this.delColumns = delColumns;
    }

    public List<MappingIndex> getUpdateIndexes() {
        return updateIndexes;
    }

    public void setUpdateIndexes(List<MappingIndex> updateIndexes) {
        this.updateIndexes = updateIndexes;
    }

    public List<MappingIndex> getNewIndexes() {
        return newIndexes;
    }

    public void setNewIndexes(List<MappingIndex> newIndexes) {
        this.newIndexes = newIndexes;
    }

    public List<String> getDropIndexes() {
        return dropIndexes;
    }

    public void setDropIndexes(List<String> dropIndexes) {
        this.dropIndexes = dropIndexes;
    }
}
