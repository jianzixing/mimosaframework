package org.mimosaframework.orm.sql.test;

import org.mimosaframework.orm.sql.test.delete.DeleteFactory;
import org.mimosaframework.orm.sql.test.select.SelectFactory;

public class TestSQL {
    public static void main(String[] args) {
        SelectFactory.select().fields().from().table(TestSQL.class).where();
        DeleteFactory.delete().table(TestSQL.class).from().table(TestSQL.class).where();
        DeleteFactory.delete().from().table(TestSQL.class)
                .where()
                .gt("", "")
                .and()
                .wrapper().and().wrapper().and()
                .in("", "")
                .orderBy().limit();

        DeleteFactory.delete().from()
                .table(TestSQL.class)
                .using(TestSQL.class)
                .where();

        DeleteFactory.delete().tables("").from().table(TestSQL.class).where().eq("", "");
    }
}
