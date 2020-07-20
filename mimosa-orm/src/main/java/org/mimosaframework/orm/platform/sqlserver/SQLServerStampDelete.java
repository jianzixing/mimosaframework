package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.platform.*;

public class SQLServerStampDelete extends PlatformStampDelete {
    public SQLServerStampDelete(PlatformStampSection section,
                                PlatformStampReference reference,
                                PlatformDialect dialect,
                                PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
