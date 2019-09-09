package org.mimosaframework.orm.auxiliary;

public interface SwitchFactory {
    Switch build(String group);

    void registerNotify(SwitchChangedListener listener);
}
