package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionHolder;

import java.sql.SQLException;
import java.util.*;

// 事务资源管理器
public class TransactionManagerUtils {
    // 每个事务API管理器，只要新建了TransactionManager就视为开始了事务
    private static final ThreadLocal<List<TransactionManager>> resources = new ThreadLocal<>();
    // 每个Session对应的事务资源管理器，其实和holders可以合并，前提是单机情况，如果有多数据源则必须这样
    private static final ThreadLocal<List<Transaction>> transactions = new ThreadLocal<>();
    // Session资源管理器
    private static final ThreadLocal<Map<SessionFactory, SessionHolder>> holders = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> rollbackMark = new ThreadLocal<>();
    private static final ThreadLocal<TransactionIsolationType> transIsolation = new ThreadLocal<>();

    public static void register(TransactionManager transactionManager) {
        List<TransactionManager> list = resources.get();
        if (list == null) {
            list = new ArrayList<>();
            resources.set(list);
        }
        list.add(transactionManager);
    }

    public static int release(TransactionManager transactionManager) {
        List<TransactionManager> list = resources.get();
        if (list != null) {
            list.remove(transactionManager);
            int size = list.size();
            if (size == 0) {
                resources.remove();
            }
            return size;
        }
        return 0;
    }

    public static void markRollback() {
        rollbackMark.set(true);
    }

    public static boolean isMarkRollback() {
        Boolean bool = rollbackMark.get();
        return bool != null && bool ? true : false;
    }

    public static void clearMarkRollback() {
        rollbackMark.remove();
    }

    public static boolean isTransactional() {
        if (resources.get() != null && resources.get().size() > 0) return true;
        return false;
    }

    public static void setTransIsolation(TransactionIsolationType iso) {
        if (transIsolation.get() == null) {
            transIsolation.set(iso);
        }
    }

    public static TransactionIsolationType getTransIsolation() {
        return transIsolation.get();
    }

    public static void clearTransIsolation() {
        transIsolation.remove();
    }

    public static void bindIfTransactional(Transaction transaction) throws SQLException {
        if (isTransactional()) {
            bind(transaction);
        }
    }

    public static void bind(Transaction transaction) throws SQLException {
        List<Transaction> list = transactions.get();
        if (list == null) {
            list = new ArrayList<>();
            transactions.set(list);
        }
        if (list.indexOf(transaction) == -1) {
            list.add(transaction);
            transaction.begin();
        }
    }

    public static void addSessionHolder(SessionFactory factory, SessionHolder sessionHolder) {
        Map<SessionFactory, SessionHolder> map = holders.get();
        if (map == null) {
            map = new HashMap<>();
            holders.set(map);
        }
        map.put(factory, sessionHolder);
    }

    public static Collection<SessionHolder> getSessionHolders() {
        Map<SessionFactory, SessionHolder> map = holders.get();
        if (map != null) {
            return map.values();
        }
        return null;
    }

    public static void clearTransactions() {
        transactions.remove();
    }

    public static void clearSessionHolders() {
        holders.remove();
    }

    public static List<Transaction> getTransactions() {
        return transactions.get();
    }

    public static int countTransactionManager() {
        return resources.get() != null ? resources.get().size() : 0;
    }

    public static boolean hasTransactionManager(DefaultTransactionManager transactionManager) {
        List<TransactionManager> list = resources.get();
        if (list != null) {
            return list.indexOf(transactionManager) >= 0;
        }
        return false;
    }
}
