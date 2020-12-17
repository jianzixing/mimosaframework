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
}
