package org.mimosaframework.orm.platform.postgresql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.List;

public class PostgreSQLCarryHandler extends CarryHandler {
    private static final Log logger = LogFactory.getLog(PostgreSQLCarryHandler.class);

    public PostgreSQLCarryHandler(ActionDataSourceWrapper dswrapper) {
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
                    || changerClassify == ChangerClassify.CREATE
                    || changerClassify == ChangerClassify.DROP) {
                dbSession.execute(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
            } else if (changerClassify == ChangerClassify.ADD_OBJECT
                    || changerClassify == ChangerClassify.INSERT) {
                List<Long> backObjects = dbSession.insert(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                if (backObjects != null && backObjects.size() > 0) {
                    return backObjects.get(0);
                }
            } else if (changerClassify == ChangerClassify.ADD_OBJECTS) {
                List<Long> backObjects = dbSession.insert(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                return backObjects;
            } else if (changerClassify == ChangerClassify.UPDATE_OBJECT
                    || changerClassify == ChangerClassify.UPDATE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                return dbSession.update(structure);
            } else if (changerClassify == ChangerClassify.DELETE_OBJECT
                    || changerClassify == ChangerClassify.DELETE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                return dbSession.delete(structure);
            } else if (changerClassify == ChangerClassify.SELECT
                    || changerClassify == ChangerClassify.COUNT
                    || changerClassify == ChangerClassify.SELECT_PRIMARY_KEY) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
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
