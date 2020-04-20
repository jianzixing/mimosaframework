package org.mimosaframework.orm.sql.rename;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class RenameFactory {
    public static RenameAnyBuilder rename() {
        RenameAnyBuilder renameAnyBuilder = SQLActionFactory.rename();
        return renameAnyBuilder;
    }
}
