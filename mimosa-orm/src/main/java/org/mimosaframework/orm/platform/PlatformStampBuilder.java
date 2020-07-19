package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.sql.stamp.*;

public interface PlatformStampBuilder {
    PlatformStampAlter alter();

    PlatformStampCreate create();

    PlatformStampDrop drop();

    PlatformStampRename rename();

    PlatformStampInsert insert();

    PlatformStampDelete delete();

    PlatformStampSelect select();

    PlatformStampUpdate update();

    StampCombineBuilder structure();
}
