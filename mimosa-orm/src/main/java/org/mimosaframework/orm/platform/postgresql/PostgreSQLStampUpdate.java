package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.platform.*;

public class PostgreSQLStampUpdate extends PlatformStampUpdate {
    public PostgreSQLStampUpdate(PlatformStampSection section,
                                 PlatformStampReference reference,
                                 PlatformDialect dialect,
                                 PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
