package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.orm.platform.*;

public class SqliteStampDelete extends PlatformStampDelete {
    public SqliteStampDelete(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
