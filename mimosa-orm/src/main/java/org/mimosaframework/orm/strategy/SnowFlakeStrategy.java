package org.mimosaframework.orm.strategy;

import org.mimosaframework.core.utils.SnowFlake;
import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.exception.StrategyException;

import java.io.Serializable;

public class SnowFlakeStrategy implements IDStrategy {
    protected SnowFlake snowFlake = null;

    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        if (snowFlake == null) {
            synchronized (this) {
                if (snowFlake == null) snowFlake = this.buildSnowFlack();
            }
        }
        return this.snowFlake.nextId();
    }

    public SnowFlake buildSnowFlack() {
        return new SnowFlake();
    }
}
