package org.mimosaframework.orm;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingTable;

import java.util.ArrayList;
import java.util.List;

public class DynamicTable {
    private String tableName;
    private String charset = "UTF8";
    private String engineName;
    private List<DynamicTableItem> tableItems;

    public static DynamicTable build() {
        return new DynamicTable();
    }

    public static DynamicTable build(String tableName) {
        return new DynamicTable(tableName);
    }

    public static DynamicTable build(String tableName, List<DynamicTableItem> tableItems) {
        return new DynamicTable(tableName, tableItems);
    }

    public DynamicTable() {
    }

    public DynamicTable(String tableName) {
        this.tableName = tableName;
    }

    public DynamicTable(String tableName, List<DynamicTableItem> tableItems) {
        this.tableName = tableName;
        this.tableItems = tableItems;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public List<DynamicTableItem> getTableItems() {
        return tableItems;
    }

    public void setTableItems(List<DynamicTableItem> tableItems) {
        this.tableItems = tableItems;
    }

    public void addItem(DynamicTableItem item) {
        if (this.tableItems == null) {
            this.tableItems = new ArrayList<>();
        }
        this.tableItems.add(item);
    }

    public MappingTable toMappingTable() {
        SpecificMappingTable table = new SpecificMappingTable();
        table.setMappingTableName(tableName);
        table.setSourceMappingTableName(tableName);
        table.setEncoding(charset);
        table.setEngineName(engineName);
        if (this.tableItems != null) {
            for (DynamicTableItem item : this.tableItems) {
                table.addMappingField(item.toMappingField());
            }
        }
        return table;
    }
}
