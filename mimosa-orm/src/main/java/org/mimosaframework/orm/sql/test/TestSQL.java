package org.mimosaframework.orm.sql.test;

import org.mimosaframework.orm.sql.test.create.CreateFactory;
import org.mimosaframework.orm.sql.test.delete.DeleteFactory;
import org.mimosaframework.orm.sql.test.insert.InsertFactory;
import org.mimosaframework.orm.sql.test.select.SelectFactory;
import org.mimosaframework.orm.sql.test.update.UpdateFactory;

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

        ///

        SelectFactory.select()
                .fields(FieldItems.build().field("", "").field("", ""))
                .from()
                .table(TestSQL.class, TestSQL.class)
                .where().eq("", "").and().eq("", "");

        SelectFactory.select().all().from()
                .table(TableItems.build().table(TestSQL.class))
                .left().join().table(TestSQL.class).on().eq("", "").and().wrapper().and().eq("", "")
                .inner().join().table(TestSQL.class).on().eq("", "")
                .where().eq("", "").groupBy().having().orderBy().asc().limit();


        ///

        UpdateFactory.update().table(TestSQL.class)
                .set().value("", "")
                .split().value("", "")
                .where().eq("", "")
                .orderBy().asc().limit();


        ///

        InsertFactory.insert().into().table(TestSQL.class)
                .wrapper("", "").values()
                .wrapper("", "")
                .split()
                .wrapper("", "")
                .split()
                .wrapper("", "");


        ///

        CreateFactory.create().database().if_not_exist().name("").charset("").collate("");

        CreateFactory.create().table().if_not_exist().name("").columns(
                
        ).charset("").extra("");
    }
}
