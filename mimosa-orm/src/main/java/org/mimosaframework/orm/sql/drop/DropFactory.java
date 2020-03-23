package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.platform.mysql.MysqlSQLDropBuilder;
import org.mimosaframework.orm.sql.DropBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DropFactory {
    public static DropAnyBuilder drop() {
        final DropBuilder<DropAnyBuilder> dropBuilder1 = (DropBuilder<DropAnyBuilder>) Proxy.newProxyInstance(
                DropFactory.class.getClassLoader(),
                new Class[]{DropBuilder.class, DropAnyBuilder.class, DropDatabaseBuilder.class, DropTableBuilder.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        return new MysqlSQLDropBuilder();
                        return null;
                    }
                }
        );

        DropBuilder<DropAnyBuilder> dropBuilder2 = (DropBuilder<DropAnyBuilder>) Proxy.newProxyInstance(
                DropFactory.class.getClassLoader(),
                new Class[]{DropBuilder.class, DropAnyBuilder.class, DropDatabaseBuilder.class, DropTableBuilder.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        return new MysqlSQLDropBuilder();
                        return dropBuilder1;
                    }
                }
        );
        return dropBuilder2.drop();
    }
}
