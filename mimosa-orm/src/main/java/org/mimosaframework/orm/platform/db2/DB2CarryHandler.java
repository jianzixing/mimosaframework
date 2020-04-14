package org.mimosaframework.orm.platform.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.platform.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DB2CarryHandler extends DBRunner {
    private static final Log logger = LogFactory.getLog(DB2CarryHandler.class);

    public DB2CarryHandler(DataSourceWrapper dswrapper) {
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

                dbSession.setDatabaseExecutorCallback(new DatabaseExecutorCallback() {
                    @Override
                    public void select(Connection connection, PreparedStatement statement, ResultSet resultSet, ModelObject object) throws SQLException {
                        processClobType(object);
                    }
                });

                List<ModelObject> objects = dbSession.select(structure);
                return objects;
            }
        } finally {
            if (dswrapper != null && dswrapper.isAutoCloseConnection()) {
                dswrapper.close();
            }
        }
        return null;
    }
}
