package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.List;

public class SqliteCarryHandler extends DBRunner {
    private static final Log logger = LogFactory.getLog(SqliteCarryHandler.class);

    public SqliteCarryHandler(DataSourceWrapper dswrapper) {
        super(dswrapper);
    }

    @Override
    public Object doHandler(PorterStructure structure) throws SQLException {
        JDBCExecutor dbSession = dswrapper.getDBChanger();
        try {
            TypeForRunner changerClassify = structure.getChangerClassify();
            if (changerClassify == TypeForRunner.CREATE_TABLE
                    || changerClassify == TypeForRunner.CREATE_FIELD
                    || changerClassify == TypeForRunner.DROP_TABLE
                    || changerClassify == TypeForRunner.DROP_FIELD
                    || changerClassify == TypeForRunner.CREATE
                    || changerClassify == TypeForRunner.DROP
                    || changerClassify == TypeForRunner.ALTER) {
                dbSession.execute(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
            } else if (changerClassify == TypeForRunner.ADD_OBJECT
                    || changerClassify == TypeForRunner.INSERT) {
                List<Long> backObjects = dbSession.insert(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                if (backObjects != null && backObjects.size() > 0) {
                    return backObjects.get(0);
                }
            } else if (changerClassify == TypeForRunner.ADD_OBJECTS) {
                List<Long> backObjects = dbSession.insert(structure);
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                return backObjects;
            } else if (changerClassify == TypeForRunner.UPDATE_OBJECT
                    || changerClassify == TypeForRunner.UPDATE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                return dbSession.update(structure);
            } else if (changerClassify == TypeForRunner.DELETE_OBJECT
                    || changerClassify == TypeForRunner.DELETE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do mysql carry handler action " + changerClassify.name());
                }
                return dbSession.delete(structure);
            } else if (changerClassify == TypeForRunner.SELECT
                    || changerClassify == TypeForRunner.COUNT
                    || changerClassify == TypeForRunner.SELECT_PRIMARY_KEY) {
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
