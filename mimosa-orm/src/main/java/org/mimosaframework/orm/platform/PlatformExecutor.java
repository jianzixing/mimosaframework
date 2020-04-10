package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 执行数据库数据结构校验
 * 以及CURD操作
 */
public class PlatformExecutor {

    private void compareTableStructure(MappingGlobalWrapper wrapper) {
        PlatformDialect dialect = PlatformFactory.getDialect(null);
        List<TableStructure> structures = dialect.getTableStructures();
        List<MappingTable> mappingTables = wrapper.getMappingTables();

        if (structures != null) {
            List<MappingTable> rmTab = new ArrayList<>();
            for (TableStructure structure : structures) {
                List<TableColumnStructure> columnStructures = new ArrayList<>(structure.getColumnStructures());
                List<TableConstraintStructure> constraintStructures = structure.getConstraintStructures();

                MappingTable currTable = null;
                for (MappingTable mappingTable : mappingTables) {
                    String mappingTableName = mappingTable.getMappingTableName();
                    String tableName = structure.getTableName();
                    if (tableName.equalsIgnoreCase(mappingTableName)) {
                        rmTab.add(mappingTable);
                        currTable = mappingTable;
                        break;
                    }
                }

                if (currTable != null) {
                    List<MappingField> rmCol = new ArrayList<>();
                    List<TableColumnStructure> rmSCol = new ArrayList<>();
                    Set<MappingField> mappingFields = currTable.getMappingFields();
                    if (columnStructures != null && columnStructures.size() > 0) {
                        for (TableColumnStructure columnStructure : columnStructures) {
                            if (mappingFields != null) {
                                MappingField currField = null;
                                for (MappingField field : mappingFields) {
                                    String mappingFieldName = field.getMappingFieldName();
                                    String fieldName = columnStructure.getColumnName();
                                    if (mappingFieldName.equalsIgnoreCase(fieldName)) {
                                        currField = field;
                                        rmCol.add(field);
                                        rmSCol.add(columnStructure);
                                        break;
                                    }
                                }

                                if (currField != null) {
                                    
                                }
                            }
                        }

                        mappingFields.removeAll(rmCol);
                        columnStructures.removeAll(rmSCol);
                        if (mappingFields.size() > 0) {
                            // 有新添加的字段需要添加
                        }
                        if (columnStructures.size() > 0) {
                            // 有多余的字段需要删除
                        }
                    } else {
                        // 数据库的字段没有需要重新添加全部字段
                    }
                }

                if (currTable != null) {
                    List<MappingIndex> rmIdx = new ArrayList<>();
                    Set<MappingIndex> mappingIndexes = currTable.getMappingIndexes();
                    MappingIndex currIndex = null;
                    if (mappingIndexes != null) {
                        for (MappingIndex index : mappingIndexes) {
                            String mappingIndexName = index.getIndexName();
                            List<TableIndexStructure> indexStructures = structure.getIndexStructures(mappingIndexName);

                            if (indexStructures != null) {
                                List<MappingField> indexMappingFields = index.getIndexColumns();
                                List<MappingField> rmIdxCol = new ArrayList<>();
                                for (TableIndexStructure indexStructure : indexStructures) {
                                    String indexColumnName = indexStructure.getColumnName();
                                    for (MappingField indexMappingField : indexMappingFields) {
                                        if (indexMappingField.getMappingColumnName().equalsIgnoreCase(indexColumnName)) {
                                            rmIdxCol.add(indexMappingField);
                                        }
                                    }
                                }
                                indexMappingFields.removeAll(rmIdxCol);
                                if (indexMappingFields.size() != 0) {

                                }
                            }
                        }
                    }
                }
            }
            mappingTables.removeAll(rmTab);
            if (mappingTables.size() != 0) {
                // 映射表没有添加到数据库
                // 需要新建数据库表
            }
        }
    }
}
