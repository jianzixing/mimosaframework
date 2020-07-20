package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.platform.*;

public class OracleStampDelete extends PlatformStampDelete {

    public OracleStampDelete(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
