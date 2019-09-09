package org.mimosaframework.orm.exception;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionException extends SQLException {
    private List<Exception> list;

    public void addSQLException(Exception e) {
        if (list == null) {
            list = new ArrayList<Exception>();
        }
        list.add(e);
    }

    public List<Exception> getList() {
        return list;
    }

    public TransactionException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public TransactionException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public TransactionException(String reason) {
        super(reason);
    }

    public TransactionException() {
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public TransactionException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public TransactionException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
