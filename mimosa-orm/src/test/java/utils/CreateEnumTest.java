package utils;

import org.junit.Test;
import org.mimosaframework.orm.DynamicClassBuilder;
import org.mimosaframework.orm.DynamicTableItem;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.lang.annotation.Annotation;
import java.lang.management.*;

public class CreateEnumTest {


    @Test
    public void test() throws Exception {
        for (int i = 0; i < 10000000; i++) {
            DynamicClassBuilder util = new DynamicClassBuilder("com.abc.TableHelloWorld");
            DynamicTableItem item = new DynamicTableItem();
            item.setFieldName("id");
            item.setPk(true);
            item.setStrategy(AutoIncrementStrategy.class);
            util.addItem(item);

            item = new DynamicTableItem();
            item.setFieldName("name");
            item.setLength(20);
            util.addItem(item);

            Class c = util.getEnumClass();
            Table table = (Table) c.getAnnotation(Table.class);
            Object[] objects = c.getEnumConstants();
            for (Object o : objects) {
                String enumName = ((Enum<?>) o).name();  // 先获取枚举名
                Annotation annotation = o.getClass().getField(enumName).getAnnotation(Column.class); //在获取注解
                // System.out.println(o);
            }

            if (i % 1000 == 0) {
                RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
                ThreadMXBean threads = ManagementFactory.getThreadMXBean();
                MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
                ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
                Thread.sleep(100);
                System.out.println(i + " class load count:" + cl.getLoadedClassCount());
            }
        }
    }
}
