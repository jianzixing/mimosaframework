package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.platform.PlatformStampReference;

public class OracleStampReference extends PlatformStampReference {
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
