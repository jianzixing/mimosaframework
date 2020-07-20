package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.platform.*;

public class PostgreSQLStampDelete extends PlatformStampDelete {
    public PostgreSQLStampDelete(PlatformStampSection section,
                                 PlatformStampReference reference,
                                 PlatformDialect dialect,
                                 PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
