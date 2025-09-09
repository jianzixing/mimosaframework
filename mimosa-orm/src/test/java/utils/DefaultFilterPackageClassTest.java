package utils;

import org.junit.Test;
import org.mimosaframework.core.utils.DefaultFilterPackageClass;
import org.mimosaframework.orm.annotation.Table;

import java.util.List;

public class DefaultFilterPackageClassTest {

    @Test
    public void test() {
        DefaultFilterPackageClass filter = new DefaultFilterPackageClass();
        filter.setPackagePath(List.of("tables.*.list"));
        var ls = filter.getScanClass(Table.class);
        System.out.println(ls);
    }
}
