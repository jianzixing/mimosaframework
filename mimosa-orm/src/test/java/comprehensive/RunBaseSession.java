package comprehensive;


import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableUser;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class RunBaseSession {
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
    public void save() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);
    }

    @Test
    public void saveAndUpdate() {
    }

    @Test
    public void update() {

    }


    @Test
    public void delete() {

    }

    @Test
    public void get() {

    }

    @Test
    public void list() {
    }

    @Test
    public void count() {
    }

    @Test
    public void paging() {
    }

    @Test
    public void getZipperTable() {
    }

    @Test
    public void calculate() {
    }

    @Test
    public void getAutonomously() throws Exception {
    }

    @Test
    public void getDataSourceNames() {
    }

    @Test
    public void close() throws IOException {

    }
}
