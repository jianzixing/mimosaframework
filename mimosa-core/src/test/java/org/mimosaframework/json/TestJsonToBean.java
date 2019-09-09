package org.mimosaframework.json;

import org.mimosaframework.core.json.ModelObject;

/**
 * @author yangankang
 */
public class TestJsonToBean {
    public static void main(String[] args) {
        String json = "{a:'aaa',b:'bbb',bean2:{c:'ccc',d:'ddd',bean3:{e:'eee',f:'fff'}}}";

        Bean1 bean1 = ModelObject.parseObject(json).toJavaObject(Bean1.class);
        System.out.println(bean1.getBean2().getBean3().getE());

        String json2 = "{a:1,b:'bbb',c:2,d:'ddd'}";
        BClass b = ModelObject.parseObject(json2).toJavaObject(BClass.class);
        System.out.println(ModelObject.toJSONString(b));
    }
}
