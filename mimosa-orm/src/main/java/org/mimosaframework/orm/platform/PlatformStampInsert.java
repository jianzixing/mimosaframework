package org.mimosaframework.orm.platform;

public abstract class PlatformStampInsert extends PlatformStampCommonality {
    public PlatformStampInsert(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }
}
