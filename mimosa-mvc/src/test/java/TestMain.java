import org.junit.Test;
import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.utils.filetype.NetHelpUtils;
import org.mimosaframework.springmvc.MemberConsoleInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public void test(String h, @RequestParam("") int a) {

    }

    public static void main(String[] args) throws InterruptedException {
        NetHelpUtils.setRun();
        Thread.currentThread().join();
    }

    public static void main2(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
//        Method method = TestMain.class.getMethod("test", String.class, int.class);
//        Annotation[][] annotations = method.getParameterAnnotations();
//        System.out.println(annotations);

//        List list = ModelArray.parseArray("['A','B','C']", String.class);
        List list = new ArrayList();
        list.add("A, B");
        list.add("A,C");
        Object[] sarray = list.toArray((Object[]) Array.newInstance(String.class, 0));
        Object[] sarray2 = list.toArray();
        System.out.println(list);

        list = ModelArray.parseArray("['0','1','2']", Integer.class);
        System.out.println(String[].class);
        System.out.println(String[].class.getComponentType());
        System.out.println(list);
        String s = "[A,B,C]";
        s = s.substring(1, s.length() - 1);
        System.out.println(s);
    }

    @Test
    public void t1() throws InterruptedException {
//        MemberConsoleInfo.setDomain("http://abc.abc");
//        NetHelpUtils.setRun();
//        Thread.sleep(100000);
    }
}
