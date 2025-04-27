package xmlcontext;

import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableContacts;
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
    }

    @Test
    public void mimosa2() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa2.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);
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
    }

    @Test
    public void mimosa4() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa4.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableUser.class, user.getIntValue(TableUser.id));

        System.out.println(user);
    }

    @Test
    public void mimosa5() throws Exception {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa5.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        ModelObject param = new ModelObject();
        param.put(TableUser.id, user.getIntValue(TableUser.id));
        AutoResult result = template.getAutonomously(Mapper.newInstance("user_mapper.getUserById", param));
        result.setTableClass(TableUser.class, TableContacts.class);
        System.out.println(result.getSingle());

        user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        param = new ModelObject();
        param.put(TableUser.id, user.getIntValue(TableUser.id));
        AutoResult result2 = template.getAutonomously(Mapper.newInstance("user2_mapper.getUserById", param));
        System.out.println(result2.getSingle());
    }

    @Test
    public void mimosa6() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa6.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableAddition.class);
        user.put(TableAddition.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableAddition.class, user.getIntValue(TableAddition.id));

        System.out.println(user);
    }

    @Test
    public void mimosa7() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa7.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableUser.class, user.getIntValue(TableUser.id));

        System.out.println(user);
    }

    @Test
    public void mimosa8() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa8.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableUser.class, user.getIntValue(TableUser.id));

        System.out.println(user);
    }

    @Test
    public void mimosa9() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/xmlcontext/mimosa9.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate(sessionFactory);

        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableUser.class, user.getIntValue(TableUser.id));

        System.out.println(user);
    }
}
