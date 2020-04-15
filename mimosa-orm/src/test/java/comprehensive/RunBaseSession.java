package comprehensive;


import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.*;
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
        System.out.println(user);
    }

    @Test
    public void saveAndUpdate() {
    }

    @Test
    public void update() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);

        int id = user.getIntValue(TableUser.id);
        ModelObject update = new ModelObject(TableUser.class);
        update.put(TableUser.id, id);
        update.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        update.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.update(update);

        template.update(Criteria.update(TableUser.class)
                .set(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30))
                .eq(TableUser.id, id));
    }


    @Test
    public void delete() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);

        int id = user.getIntValue(TableUser.id);
        ModelObject update = new ModelObject(TableUser.class);
        update.put(TableUser.id, id);
        update.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        update.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.delete(update);

        template.delete(Criteria.delete(TableUser.class).eq(TableUser.id, id));
    }

    @Test
    public void get() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);

        int id = user.getIntValue(TableUser.id);
        user = template.get(Criteria.query(TableUser.class).eq(TableUser.id, id));
        System.out.println(user);
        user = template.get(TableUser.class, id);
        System.out.println(user);
    }

    @Test
    public void list() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .limit(0, 10));
        System.out.println(objects);
    }

    @Test
    public void count() {
        long count = template.count(Criteria.query(TableUser.class)
                .limit(0, 10));
        System.out.println(count);
    }

    @Test
    public void paging() {
        Paging paging = template.paging(Criteria.query(TableUser.class)
                .limit(0, 10));
        System.out.println(paging);
    }

    @Test
    public void getZipperTable() {
    }

    @Test
    public void calculate() {
        AutoResult objects = template.calculate(Criteria.fun(TableUser.class)
                .addFunction(BasicFunction.AVG, TableUser.level, "level")
                .lt(TableUser.id, 1000));
        System.out.println(objects.getObjects());
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
