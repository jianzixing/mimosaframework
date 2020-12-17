package comprehensive;


import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.criteria.HavingField;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.transaction.TransactionManager;
import tables.TableOrder;
import tables.TableUser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class RunTransactionSession {
    private SessionTemplate template;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            template = RunDataSourceBuilder.currTemplate();
        }
    }

    /**
     * 测试基本事务
     */
    @Test
    public void save() {
        TransactionManager transactionManager = template.beginTransaction();
        try {
            ModelObject user = new ModelObject(TableUser.class);
            user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            template.save(user);
            user = template.get(TableUser.class, user.getLong(TableUser.id));
            System.out.println(user);
            transactionManager.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback();
        }
    }

    /**
     * 测试并发事务
     */
    @Test
    public void test1() {
        for (int i = 0; i < 200; i++) {
            TransactionManager transactionManager = template.beginTransaction();
            try {
                ModelObject user = new ModelObject(TableUser.class);
                user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
                user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
                template.save(user);
                user = template.get(TableUser.class, user.getLong(TableUser.id));
                System.out.println(user);
                transactionManager.commit();
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback();
            }
        }
    }


    /**
     * 测试并发事务2
     */
    @Test
    public void test2() {
        for (int i = 0; i < 200; i++) {
            ModelObject user = new ModelObject(TableUser.class);
            user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            template.save(user);
            user = template.get(TableUser.class, user.getLong(TableUser.id));
            System.out.println(user);
        }
    }

    /**
     * 多层事务测试
     */
    @Test
    public void test4() {
        TransactionManager transactionManager = template.beginTransaction();
        ModelObject user = new ModelObject(TableUser.class);
        ModelObject user2 = new ModelObject(TableUser.class);
        try {
            user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            template.save(user);

            TransactionManager transactionManager2 = template.beginTransaction();
            try {
                user2.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
                user2.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
                template.save(user2);
                transactionManager2.commit();
            } catch (Exception e) {
                transactionManager2.rollback();
            }
            transactionManager.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback();
        }
        user = template.get(TableUser.class, user.getLong(TableUser.id));
        System.out.println(user);
        user = template.get(TableUser.class, user2.getLong(TableUser.id));
        System.out.println(user);
    }


    /**
     * 多层事务测试回滚
     */
    @Test
    public void test5() {
        TransactionManager transactionManager = template.beginTransaction();
        ModelObject user = new ModelObject(TableUser.class);
        ModelObject user2 = new ModelObject(TableUser.class);
        try {
            user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
            template.save(user);

            TransactionManager transactionManager2 = template.beginTransaction();
            try {
                user2.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
                user2.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
                template.save(user2);
                if (true) throw new Exception("");
                transactionManager2.commit();
            } catch (Exception e) {
                transactionManager2.rollback();
            }
            transactionManager.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback();
        }
        user = template.get(TableUser.class, user.getLong(TableUser.id));
        System.out.println(user);
        user = template.get(TableUser.class, user2.getLong(TableUser.id));
        System.out.println(user);
    }
}
