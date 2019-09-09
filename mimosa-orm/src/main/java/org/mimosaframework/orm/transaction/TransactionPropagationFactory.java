package org.mimosaframework.orm.transaction;

public class TransactionPropagationFactory {

    public static TransactionPropagation getPropagation(TransactionManager previousTransaction,
                                                        TransactionPropagationType propagation,
                                                        TransactionIsolationType it) {
        if (propagation == TransactionPropagationType.PROPAGATION_REQUIRED) {
            return new RequiredTransactionPropagation(previousTransaction, it);
        } else if (propagation == TransactionPropagationType.PROPAGATION_SUPPORTS) {
            return new SupportsTransactionPropagation(previousTransaction, it);
        } else if (propagation == TransactionPropagationType.PROPAGATION_MANDATORY) {
            return new MandatoryTransactionPropagation(previousTransaction, it);
        } else if (propagation == TransactionPropagationType.PROPAGATION_REQUIRES_NEW) {
            return new RequiresNewTransactionPropagation(previousTransaction, it);
        } else if (propagation == TransactionPropagationType.PROPAGATION_NOT_SUPPORTED) {
            return new NotSupportedTransactionPropagation(previousTransaction, it);
        } else if (propagation == TransactionPropagationType.PROPAGATION_NEVER) {
            return new NeverTransactionPropagation(previousTransaction, it);
        } else if (propagation == TransactionPropagationType.PROPAGATION_NESTED) {
            return new NestedTransactionPropagation(previousTransaction, it);
        }
        return new RequiredTransactionPropagation(previousTransaction, it);
    }
}
