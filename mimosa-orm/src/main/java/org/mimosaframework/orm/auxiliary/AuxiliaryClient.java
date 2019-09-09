package org.mimosaframework.orm.auxiliary;

import org.mimosaframework.orm.Session;

import java.io.Closeable;

public interface AuxiliaryClient extends Closeable {
    void setSession(Session session);

    <T> T getClient();

    boolean isClose();
}
