package xmlcontext;

import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableUser;

public class XmlAppContextTesting {

    @Test
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa1.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        MimosaDataSource.clearAllDataSources();
    }
}
