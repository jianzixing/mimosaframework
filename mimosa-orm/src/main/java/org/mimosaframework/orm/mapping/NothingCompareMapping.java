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
                    public void checking(CompareUpdateTableMate tableMate) throws SQLException {
                        boolean isRebuildTable = false;
                        // create table
                        if (tableMate.getCreateTable() != null) {
                            MappingTable mappingTable = tableMate.getCreateTable();
                            if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE) {
                                DialectNextStep step = executor.createTable(mappingTable);
                                if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                logger.warn(I18n.print("compare_mapping_warn_create_table",
                                        mappingTable.getMappingTableName()));
                            }
                        }


                        // update fields
                        if (tableMate.getUpdateFields() != null && tableMate.getUpdateFields().size() > 0) {
                            MappingTable mappingTable = tableMate.getMappingTable();
                            TableStructure tableStructure = tableMate.getStructure();
                            Map<MappingField, CompareUpdateMate> updateFields = tableMate.getUpdateFields();
                            if (mappingLevel == MappingLevel.UPDATE) {
                                Iterator<Map.Entry<MappingField, CompareUpdateMate>> iterator = updateFields.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<MappingField, CompareUpdateMate> entry = iterator.next();
                                    MappingField mappingField = entry.getKey();
                                    CompareUpdateMate compareUpdateMate = entry.getValue();

                                    DialectNextStep step = executor.modifyField(mappingTable, tableStructure,
                                            mappingField, compareUpdateMate.getStructure());
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                Iterator<Map.Entry<MappingField, CompareUpdateMate>> iterator = updateFields.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<MappingField, CompareUpdateMate> entry = iterator.next();
                                    MappingField mappingField = entry.getKey();
                                    CompareUpdateMate compareUpdateMate = entry.getValue();
                                    StringBuilder sb = new StringBuilder();
                                    Iterator<ColumnEditType> typeIterator = compareUpdateMate.getEditTypes().iterator();
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


                        // create field
                        if (tableMate.getCreateFields() != null && tableMate.getCreateFields().size() > 0) {
                            MappingTable mappingTable = tableMate.getMappingTable();
                            TableStructure tableStructure = tableMate.getStructure();

                            List<MappingField> createFields = tableMate.getCreateFields();
                            if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE) {
                                for (MappingField mappingField : createFields) {
                                    DialectNextStep step = executor.createField(mappingTable, tableStructure, mappingField);
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                for (MappingField mappingField : createFields) {
                                    logger.warn(I18n.print("compare_mapping_warn_field_add",
                                            mappingTable.getMappingTableName(),
                                            mappingField.getMappingColumnName()));
                                }
                            }
                        }


                        // del fields
                        if (tableMate.getDelColumns() != null && tableMate.getDelColumns().size() > 0) {
                            List<TableColumnStructure> delColumns = tableMate.getDelColumns();
                            MappingTable mappingTable = tableMate.getMappingTable();
                            if (mappingLevel == MappingLevel.UPDATE) {
                                TableStructure tableStructure = tableMate.getStructure();
                                for (TableColumnStructure columnStructure : delColumns) {
                                    DialectNextStep step = executor.dropField(mappingTable, tableStructure, columnStructure);
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                for (TableColumnStructure columnStructure : delColumns) {
                                    logger.warn(I18n.print("compare_mapping_warn_field_del",
                                            mappingTable.getMappingTableName(),
                                            columnStructure.getColumnName()));
                                }
                            }
                        }


                        // update index
                        if (tableMate.getUpdateIndexes() != null && tableMate.getUpdateIndexes().size() > 0) {
                            List<MappingIndex> indices = tableMate.getUpdateIndexes();
                            MappingTable mappingTable = tableMate.getMappingTable();
                            if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE) {
                                for (MappingIndex index : indices) {
                                    DialectNextStep step = executor.dropIndex(mappingTable, index.getIndexName());
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                                for (MappingIndex mappingIndex : indices) {
                                    DialectNextStep step = executor.createIndex(mappingTable, mappingIndex);
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                for (MappingIndex mappingIndex : indices) {
                                    logger.warn(I18n.print("compare_mapping_warn_index_update",
                                            mappingTable.getMappingTableName(),
                                            mappingIndex.getIndexName()));
                                }
                            }
                        }

                        // create index
                        if (tableMate.getNewIndexes() != null && tableMate.getNewIndexes().size() > 0) {
                            List<MappingIndex> newIndexes = tableMate.getNewIndexes();
                            MappingTable mappingTable = tableMate.getMappingTable();
                            if (mappingLevel == MappingLevel.CREATE || mappingLevel == MappingLevel.UPDATE) {
                                for (MappingIndex mappingIndex : newIndexes) {
                                    DialectNextStep step = executor.createIndex(mappingTable, mappingIndex);
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                for (MappingIndex mappingIndex : newIndexes) {
                                    logger.warn(I18n.print("compare_mapping_warn_index_add",
                                            mappingTable.getMappingTableName(),
                                            mappingIndex.getIndexName()));
                                }
                            }
                        }

                        // drop index
                        if (tableMate.getDropIndexes() != null && tableMate.getDropIndexes().size() > 0) {
                            List<String> dropIndexes = tableMate.getDropIndexes();
                            MappingTable mappingTable = tableMate.getMappingTable();
                            if (mappingLevel == MappingLevel.UPDATE) {
                                for (String indexName : dropIndexes) {
                                    DialectNextStep step = executor.dropIndex(mappingTable, indexName);
                                    if (step == DialectNextStep.REBUILD) isRebuildTable = true;
                                }
                            }

                            if (mappingLevel == MappingLevel.WARN) {
                                for (String indexName : dropIndexes) {
                                    logger.warn(I18n.print("compare_mapping_warn_index_add",
                                            mappingTable.getMappingTableName(),
                                            indexName));
                                }
                            }
                        }

                        if (isRebuildTable && mappingLevel == MappingLevel.UPDATE) {
                            executor.doDialectRebuild(tableMate.getMappingTable(), tableMate.getStructure());
                        }
                    }
                });
    }
}
