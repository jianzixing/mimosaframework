package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.orm.platform.*;

public class SqliteStampUpdate extends PlatformStampUpdate {
    public SqliteStampUpdate(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
