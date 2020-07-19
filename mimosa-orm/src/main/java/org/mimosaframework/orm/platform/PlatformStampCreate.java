package org.mimosaframework.orm.platform;

public abstract class PlatformStampCreate extends PlatformStampCommonality {
    public PlatformStampCreate(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
