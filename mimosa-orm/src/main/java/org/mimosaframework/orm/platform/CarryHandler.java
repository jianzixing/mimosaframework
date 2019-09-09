package org.mimosaframework.orm.platform;

import java.sql.SQLException;

public abstract class CarryHandler {
    protected ActionDataSourceWrapper dswrapper;

    public CarryHandler(ActionDataSourceWrapper dswrapper) {
        this.dswrapper = dswrapper;
    }

    public abstract Object doHandler(PorterStructure[] structures) throws SQLException;
}
