package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.DifferentColumn;
import org.mimosaframework.orm.platform.PlatformFactory;

import java.util.Set;

public class MappingTableWrapper {
    private Set<MappingTable> mappingTables;

    public MappingTableWrapper(Set<MappingTable> mappingTables) {
        this.mappingTables = mappingTables;
    }

    public NotMatchObject getMissingObject(MimosaDataSource dataSource, MappingDatabase database) {
        Set<MappingTable> mappingTables = database.getDatabaseTables();
        NotMatchObject object = new NotMatchObject();
        if (this.mappingTables != null) {
            for (MappingTable classTable : this.mappingTables) {
                boolean isContains = false;
                Set<MappingField> dbFields = null;
                if (mappingTables != null) {
                    for (MappingTable table : mappingTables) {
                        if (classTable.getMappingTableName().equalsIgnoreCase(table.getDatabaseTableName())) {
                            isContains = true;
                            // 获取数据库的所有列，如果或得到了就跳出
                            dbFields = table.getMappingColumns();
                            break;
                        }
                    }
                }
                if (!isContains) {
                    object.addMissingTable(classTable);
                } else {
                    // 如果数据库中存在表那么就校验字段
                    Set<MappingField> fields = classTable.getMappingFields();
                    if (fields != null) {
                        for (MappingField f : fields) {
                            if (dbFields != null) {
                                boolean isContainsField = false;
                                MappingField containsField = null;
                                for (MappingField dbf : dbFields) {
                                    if (f.getMappingColumnName().equalsIgnoreCase(dbf.getDatabaseColumnName())) {
                                        isContainsField = true;
                                        containsField = dbf;
                                    }
                                }
                                if (!isContainsField) {
                                    object.addMissingField(f);
                                } else {
                                    DifferentColumn differentColumn = PlatformFactory.getDifferentColumn(dataSource);
                                    if (differentColumn == null) {
                                        throw new IllegalArgumentException("没有找到数据库和映射字段对比类实现");
                                    }
                                    if (!differentColumn.isLikeColumnName(f.getMappingColumnName(), containsField.getDatabaseColumnName())) {
                                        object.addChangeField(f);
                                    }
                                    if (!differentColumn.isLikeTypeName(containsField.getDatabaseColumnTypeName(),
                                            f.getMappingFieldType(), containsField.getDatabaseColumnDataType())) {
                                        object.addChangeField(f);
                                    }
                                    if (!differentColumn.isLikeAutoIncrement(containsField.getDatabaseColumnAutoIncrement(),
                                            f.isMappingFieldAutoIncrement())) {
                                        object.addChangeField(f);
                                    }
                                    if (!differentColumn.isLikeLength(containsField.getDatabaseColumnLength(),
                                            containsField.getDatabaseColumnDecimalDigits(),
                                            f.getMappingFieldLength(), f.getMappingFieldDecimalDigits())) {
                                        object.addChangeField(f);
                                    }
                                    if (!differentColumn.isLikeNullable(containsField.getDatabaseColumnNullable(),
                                            f.isMappingFieldNullable())) {
                                        object.addChangeField(f);
                                    }
                                    if (!differentColumn.isLikeDefaultValue(containsField.getDatabaseColumnDefaultValue(),
                                            f.getMappingFieldDefaultValue())) {
                                        object.addChangeField(f);
                                    }
                                    if (!differentColumn.isLikeComment(containsField.getDatabaseColumnComment(),
                                            f.getMappingFieldComment())) {
                                        object.addChangeField(f);
                                    }
                                }
                            } else {
                                object.addMissingField(f);
                            }
                        }
                    }
                }
            }
        }
        return object;
    }

    public MappingTable getCloneMappingTable(SupposedTables t) {
        Class tableClass = t.getTableClass();
        String tableName = t.getTableName();
        boolean isSplitTable = t.isSplitTable();
        String splitTableName = t.getSplitName();

        if (mappingTables != null) {
            for (MappingTable table : mappingTables) {
                if (table.getMappingClass() == tableClass) {
                    MappingTable clone = table.clone();
                    if (isSplitTable) {
                        clone.setMappingTableName(splitTableName);
                    }
                    return clone;
                }
            }

            for (MappingTable table : mappingTables) {
                if (table.getMappingTableName() == tableName) {
                    MappingTable clone = table.clone();
                    if (isSplitTable) {
                        clone.setMappingTableName(splitTableName);
                    }
                    return clone;
                }
            }
        }
        return null;
    }
}
