package xmlcontext;

import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableUser;

public class XmlAppContextTesting {

    @Test
    public void mimosa1() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa1.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        MimosaDataSource.clearAllDataSources();
    }

    @Test
    public void mimosa2() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa2.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        MimosaDataSource.clearAllDataSources();
    }

    @Test
    public void mimosa3() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa3.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableUser.class, user.getIntValue(TableUser.id));

        System.out.println(user);

        MimosaDataSource.clearAllDataSources();
    }
}
