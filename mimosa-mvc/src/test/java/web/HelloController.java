package web;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.springmvc.APIController;
import org.mimosaframework.springmvc.CURDPrinter;
import org.mimosaframework.springmvc.Printer;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@APIController
public class HelloController {

    @CURDPrinter(pk = "id")
    private String curd;

    @Printer
    public String getName(String name) {
        return "Hello " + name;
    }

    @Printer
    public String setJson(String name, ModelObject object) {
        if (object != null) {
            System.out.println(object.toJSONString());
            return "Hello " + object.toJSONString();
        }
        return name;
    }

    @Printer
    public String setWORDAction(String name, ModelObject object) {
        if (object != null) {
            System.out.println(object.toJSONString());
            return "Hello " + object.toJSONString();
        }
        return name;
    }

    @Printer
    public ResponseMessage response(String name) {
        return new ResponseMessage(100, name);
    }

    @Printer
    public void list(List<String> strings) {
        System.out.println(ModelObject.toJSONString(strings));
    }

    @Printer
    public void del(List<Integer> ids) {
        System.out.println(ModelObject.toJSONString(ids));
    }

    @Printer(path = "/{code}")
    public String setPath(@PathVariable(name = "code") String code) {
        System.out.println(code);
        return code;
    }
}
