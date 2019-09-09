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

public class DistributedSessionTesting {
    private SessionFactory sessionFactory = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-mimosa.xml"));
        sessionFactory = context.getSessionFactoryBuilder().build();
    }

    @Test
    public void testSimpleSave() throws MimosaException, IOException {
        Session session = sessionFactory.openSession();
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, RandomUtils.randomLetter(6, 32));
        object.put(TableUser.password, RandomUtils.randomIgnoreCaseLetter(6, 32));
        object.put(TableUser.realName, RandomUtils.randomChineseCharacters(3, 5));
        object.put(TableUser.address, RandomUtils.randomIgnoreCaseLetter(8, 16));
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        session.save(object);
        session.close();
    }
}
