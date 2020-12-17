package springcontext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.SessionTemplate;
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
        try {
            ModelObject user = new ModelObject(TableUser.class);
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
