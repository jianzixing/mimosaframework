package session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tables.TableUser;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-config.xml")
public class SpringContextTesting {

    @Autowired
    SessionTemplate template;

    @Test
    public void test() {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang2");
        object.put(TableUser.password, "1234562");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);
    }

    @Test
    public void test2() {
        template.update(Criteria.update(TableUser.class)
                .eq(TableUser.id, 1)
                .value(TableUser.age, 26)
        );
    }
}
