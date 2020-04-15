package comprehensive;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import tables.TablePay;
import tables.TableUser;

import java.util.List;

public class RunJoinSession {
    private SessionTemplate template;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            String config = "/template-mimosa.xml";
            XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream(config));
            SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
            template = new MimosaSessionTemplate();
            ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        }
    }

    @Test
    public void testLeftJoin() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(Criteria.leftJoin(TablePay.class).on(TableUser.id, TablePay.userId).single().aliasName("pays"))
                .limit(0, 20));
        System.out.println(objects);
    }
}
