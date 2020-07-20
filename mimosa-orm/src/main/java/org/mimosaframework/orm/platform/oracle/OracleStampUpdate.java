package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.platform.*;

public class OracleStampUpdate extends PlatformStampUpdate {

    public OracleStampUpdate(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
