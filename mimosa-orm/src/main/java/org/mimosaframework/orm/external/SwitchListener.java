package org.mimosaframework.orm.external;

import org.mimosaframework.core.json.ModelObject;

/**
 * 程序开关功能监听器
 * <p>
 * 程序可以定义开关，然后注册到配置中心去，配置中心有各种不同的开关供
 * 开启或者关闭，每一次状态转变都会通知接口进行处理，开启或者关闭也可以
 * 提供一个ModelObject类型的参数
 */
public interface SwitchListener {
    void before(Status status, ModelObject object);

    void changed(Status status, ModelObject object);

    void after(Status status, ModelObject object);

    enum Status {
        ON,
        OFF
    }
}
