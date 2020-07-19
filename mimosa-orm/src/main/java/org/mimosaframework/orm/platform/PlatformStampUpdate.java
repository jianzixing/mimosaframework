package org.mimosaframework.orm.platform;

public abstract class PlatformStampUpdate extends PlatformStampCommonality {
    public PlatformStampUpdate(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
