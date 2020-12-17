package org.mimosaframework.orm.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public class DefaultDBRunner extends DBRunner {
    private static final Log logger = LogFactory.getLog(DefaultDBRunner.class);

    public DefaultDBRunner(SessionContext sessionContext) {
        super(sessionContext);
    }

    @Override
    public Object doHandler(JDBCTraversing structure) throws SQLException {
        JDBCExecutor dbSession = sessionContext.getDBChanger();
        TypeForRunner typeForRunner = structure.getTypeForRunner();
        if (typeForRunner == TypeForRunner.CREATE
                || typeForRunner == TypeForRunner.DROP
                || typeForRunner == TypeForRunner.ALTER
                || typeForRunner == TypeForRunner.OTHER) {
            if (logger.isDebugEnabled()) {
                logger.debug("do mysql carry handler action " + typeForRunner.name());
            }
            return dbSession.execute(structure);
        } else if (typeForRunner == TypeForRunner.INSERT) {
            List<Long> backObjects = dbSession.insert(structure);
            if (logger.isDebugEnabled()) {
                logger.debug("do mysql carry handler action " + typeForRunner.name());
            }
            if (backObjects != null && backObjects.size() > 0) {
                return backObjects;
            }
        } else if (typeForRunner == TypeForRunner.UPDATE) {
            if (logger.isDebugEnabled()) {
                logger.debug("do mysql carry handler action " + typeForRunner.name());
            }
            return dbSession.update(structure);
        } else if (typeForRunner == TypeForRunner.DELETE) {
            if (logger.isDebugEnabled()) {
                logger.debug("do mysql carry handler action " + typeForRunner.name());
            }
            return dbSession.delete(structure);
        } else if (typeForRunner == TypeForRunner.SELECT) {
            if (logger.isDebugEnabled()) {
                logger.debug("do mysql carry handler action " + typeForRunner.name());
            }
            return dbSession.select(structure);
        }
        return null;
    }
}
