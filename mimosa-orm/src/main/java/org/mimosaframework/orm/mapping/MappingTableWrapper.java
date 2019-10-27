package org.mimosaframework.orm.mapping;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
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
        object.setMatchMappingTables(mappingTables);
        object.setMimosaDataSource(dataSource);
        object.setMappingDatabase(database);

        if (this.mappingTables != null) {
            for (MappingTable classTable : this.mappingTables) {
                boolean isContains = false;
                Set<MappingField> dbFields = null;
                if (mappingTables != null) {
                    for (MappingTable table : mappingTables) {
                        if (StringTools.isEmpty(classTable.getMappingTableName())) {
                            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                                    MappingTableWrapper.class, "mapping_defect_name"));
                        }
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
                                        throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                                                MappingTableWrapper.class, "not_fount_different_column"));
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
}
