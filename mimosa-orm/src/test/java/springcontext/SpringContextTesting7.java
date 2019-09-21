package springcontext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.SessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xmlcontext.TableAddition;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springcontext/spring-config7.xml")
public class SpringContextTesting7 {

    @Autowired
    SessionTemplate template;

    @Test
    public void test() throws Exception {

        ModelObject user = new ModelObject(TableAddition.class);
        user.put(TableAddition.userName, RandomUtils.randomAlphanumericLetter(10));
        template.save(user);

        user = template.get(TableAddition.class, user.getIntValue(TableAddition.id));

        System.out.println(user);

    }
}
