package org.mimosaframework.orm.sql.test;

import org.mimosaframework.orm.sql.test.delete.DeleteFactory;
import org.mimosaframework.orm.sql.test.select.SelectFactory;

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


        SelectFactory.select()
                .fields(FieldItems.build().field("", "").field("", ""))
                .from()
                .table(TestSQL.class, TestSQL.class)
                .where().eq("", "").and().eq("", "");

        SelectFactory.select().all().from()
                .table(TestSQL.class)
                .left().join().table(TestSQL.class).on().eq("", "").and().wrapper().and().eq("", "")
                .inner().join().table(TestSQL.class).on().eq("", "")
                .where().eq("", "");
    }
}
