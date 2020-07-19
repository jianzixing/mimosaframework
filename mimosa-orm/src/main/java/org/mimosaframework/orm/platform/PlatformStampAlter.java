package org.mimosaframework.orm.platform;

public abstract class PlatformStampAlter extends PlatformStampCommonality {
    public PlatformStampAlter(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
