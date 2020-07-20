package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.platform.*;

public class DB2StampUpdate extends PlatformStampUpdate {
    public DB2StampUpdate(PlatformStampSection section,
                          PlatformStampReference reference,
                          PlatformDialect dialect,
                          PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
