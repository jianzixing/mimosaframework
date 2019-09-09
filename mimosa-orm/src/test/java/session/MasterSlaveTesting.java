package session;

import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.MimosaSessionTemplate;
import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionFactoryBuilder;
import org.mimosaframework.orm.XmlAppContext;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import tables.TablePay;
import tables.TableUser;

import java.util.List;

public class MasterSlaveTesting {

    @Test
    public void t1() throws InterruptedException, ContextException {
        MimosaSessionTemplate template;
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/master-slave-standalone.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);

        for (int i = 0; i < 10; i++) {
            ModelObject user = new ModelObject(TableUser.class);
            user.put(TableUser.userName, "yangankang");
            user.put(TableUser.password, "i,love,you");
            user.put(TableUser.realName, RandomUtils.randomChineseCharacters(3));
            template.save(user);

            Thread.sleep(500);
            List<ModelObject> users = template.list(Criteria.query(TableUser.class).eq(TableUser.id, user.getIntValue(TableUser.id)).slave());
            if (users == null || users.size() == 0 || users.get(0).getIntValue(TableUser.id) != user.getIntValue(TableUser.id)) {
                throw new IllegalArgumentException("MMP");
            }
        }
    }

    @Test
    public void t2() throws InterruptedException, ContextException {
        MimosaSessionTemplate template;
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/master-slave-distributed.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);

        for (int i = 0; i < 10000; i++) {
            ModelObject pay = new ModelObject(TablePay.class);
            pay.put(TablePay.userId, 1000);
            pay.put(TablePay.orderId, RandomUtils.randomNumber(10000000, 99999999));
            pay.put(TablePay.status, RandomUtils.randomNumber(10, 50));
            template.save(pay);

            Thread.sleep(500);
            List<ModelObject> pays = template.list(
                    Criteria.query(TablePay.class)
                            .eq(TablePay.id, pay.getIntValue(TablePay.id))
                            .eq(TablePay.userId, 1000)
                            .slave("slave_1")
            );
            if (pays == null || pays.size() == 0 || pays.get(0).getIntValue(TablePay.id) != pay.getIntValue(TablePay.id)) {
                throw new IllegalArgumentException("MMP");
            }
        }
    }
}
