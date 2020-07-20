package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class DB2StampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new DB2StampAlter(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new DB2StampCreate(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new DB2StampDrop(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new DB2StampRename(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new DB2StampInsert(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new DB2StampDelete(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampSelect select() {
        return new DB2StampSelect(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new DB2StampUpdate(new DB2StampSection(),
                new DB2StampReference(),
                new DB2PlatformDialect(),
                new DB2StampShare());
    }

    @Override
    public StampCombineBuilder structure() {
        return new DB2StampStructure();
    }
}
