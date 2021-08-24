package org.mimosaframework.orm.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.*;

public abstract class AbstractCompareMapping implements CompareMapping {
    private static final Log logger = LogFactory.getLog(AbstractCompareMapping.class);
    protected MappingGlobalWrapper mappingGlobalWrapper;
    protected SessionContext sessionContext;
    protected PlatformExecutor executor = null;

    protected MappingLevel mappingLevel = null;

    public AbstractCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext sessionContext) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
        this.sessionContext = sessionContext;
        this.executor = new PlatformExecutor(mappingGlobalWrapper, sessionContext);
    }

    @Override
    public void doMapping() throws ContextException {
        final TableStructure[] current = new TableStructure[1];
        try {
            this.executor.compareTableStructure(
                    new PlatformCompare() {
                        @Override
                        public void checking(CompareUpdateTableMate tableMate) throws SQLException {
                            boolean isRebuildTable = false;

                            // create table
                            if (tableMate.getCreateTable() != null) {
                                isRebuildTable = createTable(tableMate);
                            }


                            // update fields
                            if (!isRebuildTable && tableMate.getUpdateFields() != null && tableMate.getUpdateFields().size() > 0) {
                                Map<MappingField, CompareUpdateMate> updateFields = tableMate.getUpdateFields();
                                isRebuildTable = updateFields(tableMate, updateFields);
                            }


                            // create field
                            if (!isRebuildTable && tableMate.getCreateFields() != null && tableMate.getCreateFields().size() > 0) {
                                isRebuildTable = createFields(tableMate);
                            }

                            // del fields
                            if (!isRebuildTable && tableMate.getDelColumns() != null && tableMate.getDelColumns().size() > 0) {
                                isRebuildTable = deleteFields(tableMate);
                            }

                            // update index
                            if (!isRebuildTable && tableMate.getUpdateIndexes() != null && tableMate.getUpdateIndexes().size() > 0) {
                                isRebuildTable = updateIndices(tableMate);
                            }

                            // create index
                            if (!isRebuildTable && tableMate.getNewIndexes() != null && tableMate.getNewIndexes().size() > 0) {
                                isRebuildTable = createIndices(tableMate);
                            }

                            // drop index
                            if (!isRebuildTable && tableMate.getDropIndexes() != null && tableMate.getDropIndexes().size() > 0) {
                                isRebuildTable = dropIndices(tableMate);
                            }

                            if (isRebuildTable) {
                                rebuildTable(tableMate);
                            }
                        }

                        @Override
                        public void start(TableStructure structure) {
                            current[0] = structure;
                        }
                    });
        } catch (SQLException e) {
            throw new ContextException(I18n.print("compare_db_error", current[0] != null ? current[0].getTableName() : ""), e);
        }
    }

    protected boolean createTable(CompareUpdateTableMate tableMate) throws SQLException {
        return false;
    }

    /**
     * 如果怎么修改都无法满足表则删表重建(实现上会新建表并转移数据，如果数据无法转移则报错)
     * 所以一般情况下表中已经存在数据且数据类型和当前修改的数据类型无法兼容时会清空表数据并
     * 重建表
     *
     * @param tableMate
     * @throws SQLException
     */
    protected void rebuildTable(CompareUpdateTableMate tableMate) throws SQLException {
    }

    protected boolean updateFields(CompareUpdateTableMate tableMate,
                                   Map<MappingField, CompareUpdateMate> updateFields) throws SQLException {
        return false;
    }

    protected boolean createFields(CompareUpdateTableMate tableMate) throws SQLException {
        return false;
    }

    protected boolean deleteFields(CompareUpdateTableMate tableMate) throws SQLException {
        return false;
    }

    protected boolean createIndices(CompareUpdateTableMate tableMate) throws SQLException {
        return false;
    }

    protected boolean updateIndices(CompareUpdateTableMate tableMate) throws SQLException {
        return false;
    }

    protected boolean dropIndices(CompareUpdateTableMate tableMate) throws SQLException {
        return false;
    }
}
