package springcontext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.Sql;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import tables.TableUser;


/**
 * 测试Spring的事务管理
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springcontext/spring-config1.xml")
public class SpringContextTesting1 {

    @Autowired
    SessionTemplate template;

    /**
     * 测试Spring注解事务
     */
    @Test
    @Transactional
    public void test() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);
    }

    /**
     * 测试Spring的编程试事务
     */
    @Test
    public void test2() {
        TransactionManager manager = template.beginTransaction();
        ModelObject user = new ModelObject(TableUser.class);
        try {
            user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
            template.save(user);
            ModelObject object = template.get(TableUser.class, user.getLong(TableUser.id));
            System.out.println(object);
            manager.commit();
        } catch (Exception e) {
            manager.rollback();
        }
        user.remove(TableUser.id);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);
        ModelObject object = template.get(TableUser.class, user.getLong(TableUser.id));
        System.out.println(object);
    }

    /**
     * 测试数据链接
     */
    @Test
    public void test3() {
        for (int i = 0; i < 20; i++) {
            TransactionManager manager = template.beginTransaction();
            ModelObject user = new ModelObject(TableUser.class);
            try {
                user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
                template.save(user);
                ModelObject object = template.get(TableUser.class, user.getLong(TableUser.id));
                System.out.println(object);
                manager.commit();
            } catch (Exception e) {
                manager.rollback();
            }
        }
    }


    /**
     * 测试Spring的编程试事务，事务嵌套
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

    @Test
    public void test6() {
        var data = template.sql(Sql.select("""
                select * from t_user
                where id>0;
                """));
        System.out.println(ModelObject.toJSONString(data));
    }
}
