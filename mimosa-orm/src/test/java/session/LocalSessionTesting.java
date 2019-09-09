package session;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import tables.TableUser;

import java.io.IOException;
import java.util.Date;

public class LocalSessionTesting {

    private SessionFactory sessionFactory = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/local-mimosa.xml"));
        sessionFactory = context.getSessionFactoryBuilder().build();
    }

    @Test
    public void saveForNormal() throws MimosaException, IOException {
        Session session = sessionFactory.openSession();
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        session.save(object);
        session.close();
    }

    @Test
    public void saveForCloseSession() throws MimosaException, IOException {
        Session session = sessionFactory.openSession();
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, RandomUtils.randomLetter(6, 32));
        object.put(TableUser.password, RandomUtils.randomIgnoreCaseLetter(6, 32));
        object.put(TableUser.realName, RandomUtils.randomChineseCharacters(3));
        object.put(TableUser.address, RandomUtils.randomChineseCharacters(8));
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        session.save(object);
        session.close();

        boolean succ = true;
        ModelObject object2;
        try {
            object2 = new ModelObject(TableUser.class);
            object2.put(TableUser.userName, RandomUtils.randomLetter(6, 32));
            object2.put(TableUser.password, RandomUtils.randomIgnoreCaseLetter(6, 32));
            object2.put(TableUser.realName, RandomUtils.randomChineseCharacters(3));
            object2.put(TableUser.address, RandomUtils.randomChineseCharacters(8));
            object2.put(TableUser.age, 25);
            object2.put(TableUser.level, 10);
            object2.put(TableUser.createdTime, new Date());
            session.save(object2);
        } catch (Exception e) {
            succ = false;
        }
        if (succ) {
            throw new IllegalStateException("链接已经关闭才对,结果保存成功了!");
        }
    }
}
