package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class SQLServerStampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new SQLServerStampAlter(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new SQLServerStampCreate(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new SQLServerStampDrop(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new SQLServerStampRename(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new SQLServerStampInsert(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new SQLServerStampDelete(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampSelect select() {
        return new SQLServerStampSelect(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new SQLServerStampUpdate(new SQLServerStampSection(),
                new SQLServerStampReference(),
                new SQLServerPlatformDialect(),
                new SQLServerStampShare());
    }

    @Override
    public StampCombineBuilder structure() {
        return new SQLServerStampStructure();
    }
}
