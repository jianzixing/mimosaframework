package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.*;

public class MysqlStampUpdate extends PlatformStampUpdate {
    public MysqlStampUpdate(PlatformStampSection section,
                            PlatformStampReference reference,
                            PlatformDialect dialect,
                            PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
