package web;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.springmvc.APIController;
import org.mimosaframework.springmvc.ApiRequest;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@APIController
public class HelloController {

    @ApiRequest
    public String getName(String name) {
        return "Hello " + name;
    }

    @ApiRequest
    public String setJson(String name, ModelObject object) {
        if (object != null) {
            System.out.println(object.toJSONString());
            return "Hello " + object.toJSONString();
        }
        return name;
    }

    @ApiRequest
    public String setWORDAction(String name, ModelObject object) {
        if (object != null) {
            System.out.println(object.toJSONString());
            return "Hello " + object.toJSONString();
        }
        return name;
    }

    @ApiRequest
    public ResponseMessage response(String name) {
        return new ResponseMessage(100, name);
    }

    @ApiRequest
    public void list(List<String> strings) {
        System.out.println(ModelObject.toJSONString(strings));
    }

    @ApiRequest
    public void del(List<Integer> ids) {
        System.out.println(ModelObject.toJSONString(ids));
    }

    @ApiRequest(value = "/{code}")
    public String setPath(@PathVariable(name = "code") String code) {
        System.out.println(code);
        return code;
    }
}
