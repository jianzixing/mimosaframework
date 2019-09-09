package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseExecutor {
    boolean execute(PorterStructure structure) throws SQLException;

    int delete(PorterStructure structure) throws SQLException;

    List<Long> insert(PorterStructure structure) throws SQLException;

    void inserts(BatchPorterStructure structure) throws SQLException;

    List<ModelObject> select(PorterStructure structure) throws SQLException;

    int update(PorterStructure structure) throws SQLException;
}
