package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.platform.*;

public class DB2StampDelete extends PlatformStampDelete {
    public DB2StampDelete(PlatformStampSection section,
                          PlatformStampReference reference,
                          PlatformDialect dialect,
                          PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
