package org.mimosaframework.orm.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NothingCompareMapping implements StartCompareMapping {
    private static final Log logger = LogFactory.getLog(NothingCompareMapping.class);
    protected MappingGlobalWrapper mappingGlobalWrapper;
    protected DataSourceWrapper dataSourceWrapper;
    protected PlatformExecutor executor = null;

    protected MappingLevel mappingLevel = MappingLevel.NOTHING;

    public NothingCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, DataSourceWrapper dataSourceWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
        this.dataSourceWrapper = dataSourceWrapper;
        this.executor = new PlatformExecutor(mappingGlobalWrapper, dataSourceWrapper);
    }

    @Override
    public void doMapping() throws SQLException {
        this.executor.compareTableStructure(
                new PlatformCompare() {
                    @Override
                    public void tableCreate(MappingTable mappingTable) throws SQLException {
                        if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE || mappingLevel == MappingLevel.DROP) {
                            executor.createTable(mappingTable);
                        }

                        if (mappingLevel == MappingLevel.WARN) {
                            logger.warn(I18n.print("compare_mapping_warn_create_table",
                                    mappingTable.getMappingTableName()));
                        }
                    }

                    @Override
                    public void fieldUpdate(MappingTable mappingTable,
                                            TableStructure tableStructure,
                                            Map<MappingField, List<ColumnEditType>> updateFields,
                                            Map<MappingField, TableColumnStructure> columnStructures) throws SQLException {
                        if (mappingLevel == MappingLevel.UPDATE || mappingLevel == MappingLevel.DROP) {
                            Iterator<Map.Entry<MappingField, List<ColumnEditType>>> iterator = updateFields.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map.Entry<MappingField, List<ColumnEditType>> entry = iterator.next();
                                MappingField mappingField = entry.getKey();
                                List<ColumnEditType> columnEditTypes = entry.getValue();
                                executor.modifyField(mappingTable, tableStructure,
                                        mappingField, columnStructures.get(mappingField));
                            }
                        }

                        if (mappingLevel == MappingLevel.WARN) {
                            Iterator<Map.Entry<MappingField, List<ColumnEditType>>> iterator = updateFields.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map.Entry<MappingField, List<ColumnEditType>> entry = iterator.next();
                                MappingField mappingField = entry.getKey();
                                List<ColumnEditType> columnEditTypes = entry.getValue();
                                StringBuilder sb = new StringBuilder();
                                Iterator<ColumnEditType> typeIterator = columnEditTypes.iterator();
                                while (typeIterator.hasNext()) {
                                    sb.append(typeIterator.next() + "");
                                    if (typeIterator.hasNext()) sb.append(" ");
                                }
                                logger.warn(I18n.print("compare_mapping_warn_field_update",
                                        mappingTable.getMappingTableName(),
                                        mappingField.getMappingColumnName(),
                                        sb.toString()
                                ));
                            }
                        }
                    }

                    @Override
                    public void fieldAdd(MappingTable mappingTable,
                                         TableStructure tableStructure,
                                         List<MappingField> mappingFields) throws SQLException {
                        if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE || mappingLevel == MappingLevel.DROP) {
                            for (MappingField mappingField : mappingFields) {
                                executor.createField(mappingTable, tableStructure, mappingField);
                            }
                        }

                        if (mappingLevel == MappingLevel.WARN) {
                            for (MappingField mappingField : mappingFields) {
                                logger.warn(I18n.print("compare_mapping_warn_field_add",
                                        mappingTable.getMappingTableName(),
                                        mappingField.getMappingColumnName()));
                            }
                        }
                    }

                    @Override
                    public void fieldDel(MappingTable mappingTable,
                                         TableStructure tableStructure,
                                         List<TableColumnStructure> mappingFields) throws SQLException {
                        if (mappingLevel == MappingLevel.UPDATE || mappingLevel == MappingLevel.DROP) {
                            for (TableColumnStructure columnStructure : mappingFields) {
                                executor.dropField(mappingTable, tableStructure, columnStructure);
                            }
                        }

                        if (mappingLevel == MappingLevel.WARN) {
                            for (TableColumnStructure columnStructure : mappingFields) {
                                logger.warn(I18n.print("compare_mapping_warn_field_del",
                                        mappingTable.getMappingTableName(),
                                        columnStructure.getColumnName()));
                            }
                        }
                    }

                    @Override
                    public void indexUpdate(MappingTable mappingTable,
                                            List<MappingIndex> mappingIndexes,
                                            List<String> delIndexNames) throws SQLException {
                        if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE || mappingLevel == MappingLevel.DROP) {
                            for (String indexName : delIndexNames) {
                                executor.dropIndex(mappingTable, indexName);
                            }
                            for (MappingIndex mappingIndex : mappingIndexes) {
                                executor.createIndex(mappingTable, mappingIndex);
                            }
                        }

                        if (mappingLevel == MappingLevel.WARN) {
                            for (MappingIndex mappingIndex : mappingIndexes) {
                                logger.warn(I18n.print("compare_mapping_warn_index_update",
                                        mappingTable.getMappingTableName(),
                                        mappingIndex.getIndexName()));
                            }
                        }
                    }

                    @Override
                    public void indexAdd(MappingTable mappingTable,
                                         List<MappingIndex> mappingIndexes) throws SQLException {
                        if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE || mappingLevel == MappingLevel.DROP) {
                            for (MappingIndex mappingIndex : mappingIndexes) {
                                executor.createIndex(mappingTable, mappingIndex);
                            }
                        }

                        if (mappingLevel == MappingLevel.WARN) {
                            for (MappingIndex mappingIndex : mappingIndexes) {
                                logger.warn(I18n.print("compare_mapping_warn_index_add",
                                        mappingTable.getMappingTableName(),
                                        mappingIndex.getIndexName()));
                            }
                        }
                    }
                }
        );
    }
}
