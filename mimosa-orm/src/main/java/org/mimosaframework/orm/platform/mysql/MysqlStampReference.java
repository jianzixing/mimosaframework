package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.PlatformStampReference;

public class MysqlStampReference extends PlatformStampReference {
    protected String RS = "`";
    protected String RE = "`";

    @Override
    public String getWrapStart() {
        return this.RS;
    }

    @Override
    public String getWrapEnd() {
        return this.RE;
    }

    @Override
    protected boolean isUpperCase() {
        return false;
    }
}
