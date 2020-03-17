package org.mimosaframework.orm.sql.test;

import org.mimosaframework.orm.sql.test.delete.DeleteFactory;

public class TestSQL {
    public static void main(String[] args) {
        DeleteFactory.delete()
                .table(TestSQL.class)
                .from()
                .table(TestSQL.class)
                .where()
                .eq("", "");

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
                .where()
                .eq("", "");

        DeleteFactory.delete()
                .tables("")
                .from()
                .table(TestSQL.class)
                .where()
                .eq("", "");
    }
}
