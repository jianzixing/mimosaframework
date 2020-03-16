package org.mimosaframework.orm.sql.test;

public class TestSQL {
    public static void main(String[] args) {
        GlobalBuilder.select().all().from(TestSQL.class)
                .where()
                .eq("a", "b")
                .and()
                .eq("c", "d")
                .and()
                .wrapper(
                        GlobalBuilder.whereItem().eq("c", "d").and().eq("e", "f")
                )
                .and()
                .wrapper(
                        GlobalBuilder.whereItem().eq("c", "d").and().eq("e", "f")
                )
                .or()
                .eq("", "")
                .and()
                .wrapper(
                        GlobalBuilder.whereItem().wrapper(
                                GlobalBuilder.whereItem().eq("", "")
                        )
                                .and()
                                .wrapper(
                                        GlobalBuilder.whereItem().wrapper(
                                                GlobalBuilder.whereItem().eq("", "")
                                        )
                                )
                );


        GlobalBuilder.delete().table(TestSQL.class)
                .from(GlobalBuilder.fromItem(TestSQL.class)).where().eq("", "");

        GlobalBuilder.delete().from(TestSQL.class)
                .using(TestSQL.class, TestSQL.class)
                .where().eq("", "");

        GlobalBuilder.delete().from(TestSQL.class).where().eq("", "");

        GlobalBuilder.delete().from("")
                .using(GlobalBuilder.fromItem(TestSQL.class, "a"))
                .where().eq("", "");
    }
}
