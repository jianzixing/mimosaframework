package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.platform.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleCarryHandler extends CarryHandler {
    private static final Log logger = LogFactory.getLog(OracleCarryHandler.class);

    public OracleCarryHandler(ActionDataSourceWrapper dswrapper) {
        super(dswrapper);
    }

    @Override
    public Object doHandler(PorterStructure structure) throws SQLException {
        DatabaseExecutor dbSession = dswrapper.getDBChanger();
        try {
            ChangerClassify changerClassify = structure.getChangerClassify();
            if (changerClassify == ChangerClassify.CREATE_TABLE
                    || changerClassify == ChangerClassify.CREATE_FIELD
                    || changerClassify == ChangerClassify.DROP_TABLE
                    || changerClassify == ChangerClassify.DROP_FIELD
                    || changerClassify == ChangerClassify.SILENT
                    || changerClassify == ChangerClassify.CREATE
                    || changerClassify == ChangerClassify.DROP) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                if (changerClassify == ChangerClassify.SILENT) {
                    try {
                        dbSession.execute(structure);
                    } catch (Exception e) {
                        logger.error("silent do oracle execute sql error:" + e.getMessage());
                    }
                } else {
                    dbSession.execute(structure);
                }
            } else if (changerClassify == ChangerClassify.ADD_OBJECT
                    || changerClassify == ChangerClassify.INSERT) {
                SQLBuilder sqlBuilder = structure.getSqlBuilder();
                Object autoIncrementId = null; // 自增列只允许有一个
                List<SQLDataPlaceholder> placeholders = sqlBuilder.getDataPlaceholders();
                if (placeholders != null && placeholders.size() > 0) {
                    for (SQLDataPlaceholder placeholder : placeholders) {
                        if (placeholder instanceof AISQLDataPlaceholder) {
                            List<ModelObject> id = dbSession.select(
                                    new PorterStructure(
                                            ChangerClassify.SELECT, SQLBuilderFactory.createSQLBuilder().addString(((AISQLDataPlaceholder) placeholder).getSql())));
                            if (id != null && id.size() > 0) {
                                autoIncrementId = id.get(0).get("ID");
                                if (autoIncrementId instanceof BigDecimal) {
                                    autoIncrementId = ((BigDecimal) autoIncrementId).longValue();
                                }
                                placeholder.setValue(autoIncrementId);
                            } else {
                                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                                        OracleCarryHandler.class, "oracle_auto_incr_empty"));
                            }
                        }
                    }
                }
                dbSession.insert(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return autoIncrementId;
            } else if (changerClassify == ChangerClassify.ADD_OBJECTS) {
                List<Long> autoIncrementIds = null; // 自增列只允许有一个
                if (structure instanceof BatchPorterStructure) {
                    AIBatchPorterStructure batchPorterStructure = (AIBatchPorterStructure) structure;
                    String sql = batchPorterStructure.getSql();
                    MappingField field = batchPorterStructure.getField();
                    List<ModelObject> datas = batchPorterStructure.getObjects();
                    int size = datas.size();
                    for (int i = 0; i < size; i++) {
                        List<ModelObject> id = dbSession.select(
                                new PorterStructure(
                                        ChangerClassify.SELECT, SQLBuilderFactory.createSQLBuilder().addString(sql)));
                        if (id != null && id.size() > 0) {
                            if (autoIncrementIds == null) autoIncrementIds = new ArrayList<>();
                            Object autoIncrementId = id.get(0).get("ID");
                            if (autoIncrementId instanceof BigDecimal) {
                                autoIncrementId = ((BigDecimal) autoIncrementId).longValue();
                            }
                            datas.get(i).put(field.getMappingColumnName(), autoIncrementId);
                            autoIncrementIds.add((Long) autoIncrementId);
                        } else {
                            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                                    OracleCarryHandler.class, "oracle_auto_incr_empty"));
                        }
                    }

                    dbSession.inserts(batchPorterStructure);
                } else {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            OracleCarryHandler.class, "type_not_batch"));
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return autoIncrementIds;
            } else if (changerClassify == ChangerClassify.UPDATE_OBJECT
                    || changerClassify == ChangerClassify.UPDATE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return dbSession.update(structure);
            } else if (changerClassify == ChangerClassify.DELETE_OBJECT
                    || changerClassify == ChangerClassify.DELETE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do oracle carry handler action " + changerClassify.name());
                }
                return dbSession.delete(structure);
            } else if (changerClassify == ChangerClassify.SELECT
                    || changerClassify == ChangerClassify.COUNT
                    || changerClassify == ChangerClassify.SELECT_PRIMARY_KEY) {
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
