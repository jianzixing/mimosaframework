package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.platform.PlatformStampSection;

public class DB2StampSection extends PlatformStampSection {
    public DB2StampSection() {
        this.setDeclareInBegin(true);
    }
}
