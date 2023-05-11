package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.PlatformStampSelect;
import org.mimosaframework.orm.platform.PlatformStampShare;

public class MysqlStampShare extends PlatformStampShare {
    public MysqlStampShare() {
    }

    public MysqlStampShare(PlatformStampSelect select) {
        super(select);
    }
}
