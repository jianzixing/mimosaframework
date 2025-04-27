package springcontext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.AutoResult;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tables.TableContacts;
import tables.TableUser;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springcontext/spring-config5.xml")
public class SpringContextTesting5 {

    @Autowired
    SessionTemplate template;

    @Test
    public void test() throws Exception {

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
}
