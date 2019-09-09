package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.exception.StrategyException;

import java.io.Serializable;
import java.util.UUID;

public class UUIDStrategy implements IDStrategy {
    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
