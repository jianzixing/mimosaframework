package org.mimosaframework.orm.platform;

public interface DatabasePorter extends
        TableDatabasePorter,
        InsertDatabasePorter,
        UpdateDatabasePorter,
        DeleteDatabasePorter,
        SelectDatabasePorter {
    
    void setCarryHandler(CarryHandler carryHandler);
}
