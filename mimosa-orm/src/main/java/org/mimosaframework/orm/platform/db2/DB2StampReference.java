package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.platform.PlatformStampReference;

public class DB2StampReference extends PlatformStampReference {
    protected String RS = "\"";
    protected String RE = "\"";
    protected boolean isUpperCase = true;

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
        return this.isUpperCase;
    }
}
