package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.platform.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleCarryHandler extends DBRunner {
    private static final Log logger = LogFactory.getLog(OracleCarryHandler.class);

    public OracleCarryHandler(DataSourceWrapper dswrapper) {
        super(dswrapper);
    }

    @Override
    public Object doHandler(JDBCTraversing structure) throws SQLException {
        JDBCExecutor dbSession = dswrapper.getDBChanger();
        try {
            TypeForRunner changerClassify = structure.getTypeForRunner();
            if (changerClassify == TypeForRunner.CREATE_TABLE
                    || changerClassify == TypeForRunner.CREATE_FIELD
                    || changerClassify == TypeForRunner.DROP_TABLE
                    || changerClassify == TypeForRunner.DROP_FIELD
                    || changerClassify == TypeForRunner.SILENT
                    || changerClassify == TypeForRunner.CREATE
                    || changerClassify == TypeForRunner.DROP
                    || changerClassify == TypeForRunner.ALTER) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                if (changerClassify == TypeForRunner.SILENT) {
                    try {
                        dbSession.execute(structure);
                    } catch (Exception e) {
                        logger.error("silent do oracle execute sql error:" + e.getMessage());
                    }
                } else {
                    dbSession.execute(structure);
                }
            } else if (changerClassify == TypeForRunner.ADD_OBJECT
                    || changerClassify == TypeForRunner.INSERT) {
                SQLBuilder sqlBuilder = structure.getSqlBuilder();
                Object autoIncrementId = null; // 自增列只允许有一个
                List<SQLDataPlaceholder> placeholders = structure.getSqlDataPlaceholders();
                if (placeholders != null && placeholders.size() > 0) {
                    for (SQLDataPlaceholder placeholder : placeholders) {
                        if (placeholder instanceof AISQLDataPlaceholder) {
                            List<ModelObject> id = dbSession.select(
                                    new JDBCTraversing(
                                            TypeForRunner.SELECT, SQLBuilderFactory.createSQLBuilder().addString(((AISQLDataPlaceholder) placeholder).getSql())));
                            if (id != null && id.size() > 0) {
                                autoIncrementId = id.get(0).get("ID");
                                if (autoIncrementId instanceof BigDecimal) {
                                    autoIncrementId = ((BigDecimal) autoIncrementId).longValue();
                                }
                                placeholder.setValue(autoIncrementId);
                            } else {
                                throw new IllegalArgumentException(I18n.print("oracle_auto_incr_empty"));
                            }
                        }
                    }
                }
                dbSession.insert(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return autoIncrementId;
            } else if (changerClassify == TypeForRunner.ADD_OBJECTS) {
                List<Long> autoIncrementIds = null; // 自增列只允许有一个
                if (structure instanceof BatchPorterStructure) {
                    AIBatchPorterStructure batchPorterStructure = (AIBatchPorterStructure) structure;
                    String sql = batchPorterStructure.getSql();
                    MappingField field = batchPorterStructure.getField();
                    List<ModelObject> datas = batchPorterStructure.getObjects();
                    int size = datas.size();
                    for (int i = 0; i < size; i++) {
                        List<ModelObject> id = dbSession.select(
                                new JDBCTraversing(
                                        TypeForRunner.SELECT, SQLBuilderFactory.createSQLBuilder().addString(sql)));
                        if (id != null && id.size() > 0) {
                            if (autoIncrementIds == null) autoIncrementIds = new ArrayList<>();
                            Object autoIncrementId = id.get(0).get("ID");
                            if (autoIncrementId instanceof BigDecimal) {
                                autoIncrementId = ((BigDecimal) autoIncrementId).longValue();
                            }
                            datas.get(i).put(field.getMappingColumnName(), autoIncrementId);
                            autoIncrementIds.add((Long) autoIncrementId);
                        } else {
                            throw new IllegalArgumentException(I18n.print("oracle_auto_incr_empty"));
                        }
                    }

                    dbSession.inserts(batchPorterStructure);
                } else {
                    throw new IllegalArgumentException(I18n.print("type_not_batch"));
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return autoIncrementIds;
            } else if (changerClassify == TypeForRunner.UPDATE_OBJECT
                    || changerClassify == TypeForRunner.UPDATE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return dbSession.update(structure);
            } else if (changerClassify == TypeForRunner.DELETE_OBJECT
                    || changerClassify == TypeForRunner.DELETE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return dbSession.delete(structure);
            } else if (changerClassify == TypeForRunner.SELECT
                    || changerClassify == TypeForRunner.COUNT
                    || changerClassify == TypeForRunner.SELECT_PRIMARY_KEY) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return dbSession.select(structure);
            }
        } finally {
            if (dswrapper != null && dswrapper.isAutoCloseConnection()) {
                dswrapper.close();
            }
        }
        return null;
    }
}
