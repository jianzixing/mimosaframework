package web;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.springmvc.APIController;
import org.mimosaframework.springmvc.Response;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@APIController
public class HelloController {

    @Response
    public String getName(String name) {
        return "Hello " + name;
    }

    @Response
    public String setJson(String name, ModelObject object) {
        if (object != null) {
            System.out.println(object.toJSONString());
            return "Hello " + object.toJSONString();
        }
        return name;
    }

    @Response
    public String setWORDAction(String name, ModelObject object) {
        if (object != null) {
            System.out.println(object.toJSONString());
            return "Hello " + object.toJSONString();
        }
        return name;
    }

    @Response
    public ResponseMessage response(String name) {
        return new ResponseMessage(100, name);
    }

    @Response
    public void list(List<String> strings) {
        System.out.println(ModelObject.toJSONString(strings));
    }

    @Response
    public void del(List<Integer> ids) {
        System.out.println(ModelObject.toJSONString(ids));
    }

    @Response(path = "/{code}")
    public String setPath(@PathVariable(name = "code") String code) {
        System.out.println(code);
        return code;
    }
}
