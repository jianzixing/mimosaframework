package org.mimosaframework.orm.transaction;

public enum TransactionPropagationType {
    /**
     * 这个是默认的属性
     * <p>
     * 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。
     */
    PROPAGATION_REQUIRED,
    /**
     * 支持当前事务，如果当前没有事务，就以非事务方式执行。
     */
    PROPAGATION_SUPPORTS,
    /**
     * 使用当前的事务，如果当前没有事务，就抛出异常。
     */
    PROPAGATION_MANDATORY,
    /**
     * 新建事务，如果当前存在事务，把当前事务挂起。
     */
    PROPAGATION_REQUIRES_NEW,
    /**
     * 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
     */
    PROPAGATION_NOT_SUPPORTED,
    /**
     * 以非事务方式执行，如果当前存在事务，则抛出异常。
     */
    PROPAGATION_NEVER,
    /**
     * 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。
     */
    PROPAGATION_NESTED
}
