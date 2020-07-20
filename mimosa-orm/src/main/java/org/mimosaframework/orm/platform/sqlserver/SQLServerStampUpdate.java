package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.platform.*;

public class SQLServerStampUpdate extends PlatformStampUpdate {
    public SQLServerStampUpdate(PlatformStampSection section,
                                PlatformStampReference reference,
                                PlatformDialect dialect,
                                PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
