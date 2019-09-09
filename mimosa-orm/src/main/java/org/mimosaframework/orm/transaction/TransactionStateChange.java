package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.exception.TransactionException;

public interface TransactionStateChange {
    enum State {
        BEGIN, BEGIN_END,
        COMMIT, COMMIT_END,
        ROLLBACK, ROLLBACK_END,
        ROLLBACK_RESET, ROLLBACK_RESET_END,
        CLOSE
    }

    /**
     * @param state
     * @return 是否可以继续执行事务配置
     */
    boolean change(State state) throws TransactionException;
}
