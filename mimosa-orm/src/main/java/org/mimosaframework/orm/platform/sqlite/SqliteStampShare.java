package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.orm.platform.PlatformStampSelect;
import org.mimosaframework.orm.platform.PlatformStampShare;

public class SqliteStampShare extends PlatformStampShare {

    public SqliteStampShare() {
    }

    public SqliteStampShare(PlatformStampSelect select) {
        super(select);
    }
}
