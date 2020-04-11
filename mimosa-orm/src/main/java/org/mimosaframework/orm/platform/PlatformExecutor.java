package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.stamp.KeyColumnType;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.*;

/**
 * 执行数据库数据结构校验
 * 以及CURD操作
 */
public class PlatformExecutor {

    public void compareTableStructure(MappingGlobalWrapper mapping,
                                      DataSourceWrapper dswrapper,
                                      PlatformCompare compare) {
        PlatformDialect dialect = PlatformFactory.getDialect(dswrapper);
        List<TableStructure> structures = dialect.getTableStructures();
        List<MappingTable> mappingTables = mapping.getMappingTables();

        if (structures != null) {
            List<MappingTable> rmTab = new ArrayList<>();
            for (TableStructure structure : structures) {
                List<TableColumnStructure> columnStructures = new ArrayList<>(structure.getColumnStructures());

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
                        Map<MappingField, List<ColumnEditType>> updateFields = new LinkedHashMap();
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
                                    List<ColumnEditType> columnEditTypes = new ArrayList<>();
                                    ColumnType columnType = dialect.getColumnType(this.getColumnTypeByJava(currField.getMappingFieldType()));
                                    if (columnStructure.getTypeName()
                                            .equalsIgnoreCase(columnType.getTypeName())
                                            || columnStructure.getLength() != currField.getMappingFieldLength()
                                            || columnStructure.getScale() != currField.getDatabaseColumnDecimalDigits()) {
                                        columnEditTypes.add(ColumnEditType.TYPE);
                                    }

                                    if (StringTools.isEmpty(columnStructure.getIsNullable()) != currField.isMappingFieldNullable()
                                            || columnStructure.getIsNullable().equals("Y") != currField.isMappingFieldNullable()) {
                                        columnEditTypes.add(ColumnEditType.ISNULL);
                                    }
                                    if ((StringTools.isEmpty(columnStructure.getDefaultValue()) && StringTools.isNotEmpty(currField.getMappingFieldDefaultValue()))
                                            || (StringTools.isNotEmpty(columnStructure.getDefaultValue())
                                            && !columnStructure.getDefaultValue().equals(currField.getMappingFieldDefaultValue()))) {
                                        columnEditTypes.add(ColumnEditType.DEF_VALUE);
                                    }
                                    if (StringTools.isNotEmpty(columnStructure.getAutoIncrement()) == currField.isMappingAutoIncrement()
                                            || !columnStructure.getAutoIncrement().equals("Y") != currField.isMappingAutoIncrement()) {
                                        columnEditTypes.add(ColumnEditType.AUTO_INCREMENT);
                                    }
                                    if ((StringTools.isEmpty(columnStructure.getComment()) && StringTools.isNotEmpty(currField.getMappingFieldComment()))
                                            || (StringTools.isNotEmpty(columnStructure.getComment())
                                            && !columnStructure.getComment().equals(currField.getMappingFieldComment()))) {
                                        columnEditTypes.add(ColumnEditType.COMMENT);
                                    }
                                    if (currField.isMappingFieldPrimaryKey() == structure.isPrimaryKeyColumn(columnStructure.getColumnName())) {
                                        columnEditTypes.add(ColumnEditType.PRIMARY_KEY);
                                    }

                                    if (columnEditTypes.size() > 0) {
                                        // 需要修改字段
                                        updateFields.put(currField, columnEditTypes);
                                    }
                                }
                            }
                        }

                        if (updateFields != null && updateFields.size() > 0) {
                            compare.fieldUpdate(mapping, dialect, currTable, updateFields);
                        }

                        mappingFields.removeAll(rmCol);
                        columnStructures.removeAll(rmSCol);
                        if (mappingFields.size() > 0) {
                            // 有新添加的字段需要添加
                            compare.fieldAdd(mapping, dialect, currTable, new ArrayList<MappingField>(mappingFields));
                        }
                        if (columnStructures.size() > 0) {
                            // 有多余的字段需要删除
                            compare.fieldDel(mapping, dialect, currTable, columnStructures);
                        }
                    } else {
                        // 数据库的字段没有需要重新添加全部字段
                        compare.fieldAdd(mapping, dialect, currTable, new ArrayList<MappingField>(mappingFields));
                    }
                }

                if (currTable != null) {
                    Set<MappingIndex> mappingIndexes = currTable.getMappingIndexes();
                    if (mappingIndexes != null) {
                        List<MappingIndex> newIndexes = new ArrayList<>();
                        List<MappingIndex> updateIndexes = new ArrayList<>();
                        for (MappingIndex index : mappingIndexes) {
                            String mappingIndexName = index.getIndexName();
                            List<TableIndexStructure> indexStructures = structure.getIndexStructures(mappingIndexName);

                            if (indexStructures != null && indexStructures.size() > 0) {
                                List<MappingField> indexMappingFields = index.getIndexColumns();
                                if (!indexStructures.get(0).getType().equalsIgnoreCase(index.getIndexType().toString())) {
                                    // 索引类型不一致需要重建索引
                                    updateIndexes.add(index);
                                } else {
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
                                        // 需要重建索引
                                        updateIndexes.add(index);
                                    }
                                }
                            } else {
                                // 需要新建索引
                                newIndexes.add(index);
                            }
                        }
                        if (updateIndexes != null && updateIndexes.size() > 0) {
                            compare.indexUpdate(mapping, dialect, currTable, updateIndexes);
                        }
                        if (newIndexes != null && newIndexes.size() > 0) {
                            compare.indexAdd(mapping, dialect, currTable, newIndexes);
                        }
                    }
                }
            }
            mappingTables.removeAll(rmTab);
            if (mappingTables.size() != 0) {
                // 映射表没有添加到数据库
                // 需要新建数据库表

                for (MappingTable mappingTable : mappingTables) {
                    Set<MappingIndex> mappingIndex = mappingTable.getMappingIndexes();
                    compare.tableCreate(mapping, dialect, mappingTable);
                    compare.indexAdd(mapping, dialect, mappingTable, new ArrayList<MappingIndex>(mappingIndex));
                }
            }
        }
    }

    private KeyColumnType getColumnTypeByJava(Class c) {
        if (c.equals(Integer.class) || c.equals(int.class)) return KeyColumnType.INT;
        if (c.equals(String.class)) return KeyColumnType.VARCHAR;
        if (c.equals(Character.class) || c.equals(char.class)) return KeyColumnType.CHAR;
        if (c.equals(Blob.class)) return KeyColumnType.BLOB;
        if (c.equals(Text.class)) return KeyColumnType.TEXT;
        if (c.equals(Byte.class) || c.equals(byte.class)) return KeyColumnType.TINYINT;
        if (c.equals(Short.class) || c.equals(short.class)) return KeyColumnType.SMALLINT;
        if (c.equals(Long.class) || c.equals(long.class)) return KeyColumnType.BIGINT;
        if (c.equals(Float.class) || c.equals(float.class)) return KeyColumnType.FLOAT;
        if (c.equals(Double.class) || c.equals(double.class)) return KeyColumnType.DOUBLE;
        if (c.equals(BigDecimal.class)) return KeyColumnType.DECIMAL;
        if (c.equals(Boolean.class) || c.equals(boolean.class)) return KeyColumnType.BOOLEAN;
        if (c.equals(java.sql.Date.class)) return KeyColumnType.DATE;
        if (c.equals(java.sql.Time.class)) return KeyColumnType.TIME;
        if (c.equals(java.util.Date.class)) return KeyColumnType.DATETIME;
        if (c.equals(java.sql.Timestamp.class)) return KeyColumnType.TIMESTAMP;
        return null;
    }
}
