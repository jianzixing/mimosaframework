package func;

import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.criteria.Query;
import tables.BeanPay;

public class FuncTest {

    @Test
    public void t1() {
        ModelObject json = new ModelObject();
        json.put(BeanPay::getId, 1);
        json.put(new BeanPay(), 1);
        System.out.println(ClassUtils.getLambdaFnName(BeanPay::getId));
        System.out.println(json);
        System.out.println(json.get(BeanPay::getId));
    }

    @Test
    public void t2() {
        Query query = Criteria.query(BeanPay.class).eq(BeanPay::getId, 1).orderBy(BeanPay::getMoney, true);
        System.out.println(ModelObject.toJSONString(query));
    }
}
