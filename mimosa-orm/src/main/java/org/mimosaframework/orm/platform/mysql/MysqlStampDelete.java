package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.*;

public class MysqlStampDelete extends PlatformStampDelete {
    public MysqlStampDelete(PlatformStampSection section,
                            PlatformStampReference reference,
                            PlatformDialect dialect,
                            PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
