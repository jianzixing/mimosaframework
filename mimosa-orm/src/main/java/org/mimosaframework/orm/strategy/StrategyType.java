package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.IDStrategy;

public enum StrategyType {
    INCREMENT(AutoIncrementStrategy.class),
    UUID(UUIDStrategy.class);

    private Class<? extends IDStrategy> strategy;

    StrategyType(Class<? extends IDStrategy> strategy) {
        this.strategy = strategy;
    }

    public Class<? extends IDStrategy> getStrategy() {
        return strategy;
    }
}
