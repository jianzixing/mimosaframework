package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.strategy.StrategyWrapper;

import java.io.Serializable;

public interface IDStrategy {
    Serializable get(StrategyWrapper sw, Session session) throws StrategyException;
}
