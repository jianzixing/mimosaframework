package org.mimosaframework.orm.platform;

public abstract class PlatformStampSelect extends PlatformStampCommonality {
    public PlatformStampSelect(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
