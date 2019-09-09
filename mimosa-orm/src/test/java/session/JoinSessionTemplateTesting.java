package session;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import program.service.SessionTemplateServiceTesting;
import tables.TableOrder;
import tables.TablePay;
import tables.TableUser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JoinSessionTemplateTesting {
    private static SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/template-mimosa.xml"));
            SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
            template = new MimosaSessionTemplate();
            ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        }
    }

    @Test
    public void testSaveNormalTemplate() {
        List<ModelObject> objects = template.list(
                Criteria.query(TableUser.class)
                        .subjoin(TablePay.class).eq(TablePay.userId, TableUser.id).query()
                        .limit(0, 10)
        );
        System.out.println(objects);
    }
}
