package org.mimosaframework.orm.external;

import java.rmi.Remote;

public interface ConfiguredCenterInterface extends Remote {
    void registerSwitchListener(SwitchListener listener);
}
