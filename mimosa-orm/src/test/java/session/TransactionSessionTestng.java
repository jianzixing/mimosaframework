package session;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import tables.TableUser;

import java.util.Date;

/**
 * 测试事物是否关闭
 */
public class TransactionSessionTestng {

    private SessionTemplate template = null;

    @BeforeSuite
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/transaction-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
    }

    @Test(invocationCount = 100000, threadPoolSize = 10)
    public void service() throws TransactionException {
        {
            System.out.println(MimosaDataSource.getDataSourceSize());
            final ModelObject object = new ModelObject(TableUser.class);
            object.put(TableUser.userName, "yangankang");
            object.put(TableUser.password, "123456");
            object.put(TableUser.realName, "杨安康");
            object.put(TableUser.address, "北京朝阳区");
            object.put(TableUser.age, 25);
            object.put(TableUser.level, 10);
            object.put(TableUser.createdTime, new Date());
            Transaction transaction = template.beginTransaction();
            template.save(object);
            transaction.rollback();

            ModelObject object1 = template.get(TableUser.class, object.getLongValue(TableUser.id));
            if (object1 != null) {
                throw new IllegalStateException("应该回滚的不应该保存成功!");
            }
        }

        {
            ModelObject object = new ModelObject(TableUser.class);
            object.put(TableUser.userName, "yangankang");
            object.put(TableUser.password, "123456");
            object.put(TableUser.realName, "杨安康");
            object.put(TableUser.address, "北京朝阳区");
            object.put(TableUser.age, 25);
            object.put(TableUser.level, 10);
            object.put(TableUser.createdTime, new Date());

            Transaction transaction = template.beginTransaction();
            template.save(object);
            transaction.commit();

            object = template.get(TableUser.class, object.getLongValue(TableUser.id));
            if (object == null) {
                throw new IllegalStateException("事务提交后应该保存成功!");
            }
        }
    }
}
