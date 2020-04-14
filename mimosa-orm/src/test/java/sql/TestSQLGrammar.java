package sql;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.alter.AlterFactory;
import org.mimosaframework.orm.sql.create.CreateFactory;
import org.mimosaframework.orm.sql.delete.DeleteFactory;
import org.mimosaframework.orm.sql.drop.DropFactory;
import org.mimosaframework.orm.sql.insert.InsertFactory;
import org.mimosaframework.orm.sql.select.SelectFactory;
import org.mimosaframework.orm.sql.update.UpdateFactory;

import java.io.Serializable;

public class TestSQLGrammar {
    public static void main(String[] args) {
        UnifyBuilder unifyBuilder = null;

        unifyBuilder = DeleteFactory.delete().from()
                .table(TestSQLGrammar.class)
                .where()
                .column("").eq().value("");

        unifyBuilder = DeleteFactory.delete().from().table(TestSQLGrammar.class)
                .where()
                .column("")
                .gt()
                .value("")
                .and()
                .wrapper(null)
                .and()
                .wrapper(null).and()
                .column("").in().value("")
        ;

        ///

        unifyBuilder = SelectFactory.select()
                .field("", "")
                .distinct("")
                .avg(null).as("")
                .max(null).as("")
                .field("", "")
                .from()
                .table(TestSQLGrammar.class, "")
                .table(TestSQLGrammar.class)
                .where()
                .column("").eq().value("").and()
                .isNull("")
                .and().wrapper(Wrapper.build().column("").eq().value("").and().wrapper(null));

        unifyBuilder = SelectFactory.select().all().from()
                .table(TestSQLGrammar.class, "")
                .left().join().table(TestSQLGrammar.class, "").on().column("").eq().value("").and().wrapper(null).and().column("").eq().value("")
                .inner().join().table(TestSQLGrammar.class).on().column("").eq().value("")
                .where().column("").eq().value("")
                .groupBy().column("").column("")
                .having()
                .count(new FieldItem(1)).eq().value("")
                .orderBy()
                .column("").asc()
                .column("").desc()
                .limit(0, 10);

        unifyBuilder = SelectFactory.select()
                .all()
                .from()
                .table(TestSQLGrammar.class)
                .orderBy().column("").asc()
                .limit(0, 10);

        unifyBuilder = SelectFactory.select()
                .distinct("", (Serializable) "")
                .from()
                .table(SelectFactory.select().all().from().table(""));

        ///

        unifyBuilder = UpdateFactory.update()
                .table(TestSQLGrammar.class)
                .set("", "")
                .set("", "")
                .set("", "")
                .set("", "")
                .set("", "")
                .where()
                .column("").eq().value("")
                .and()
                .column("").eq().value("")
                .and()
                .column("").between().section("", "")
                .and()
                .column("", "").gt().value("")
                .or()
                .column("").eq().column("");

        ///

        unifyBuilder = InsertFactory.insert().into().table(TestSQLGrammar.class)
                .columns("", "")
                .values()
                .row("", "");

        unifyBuilder = InsertFactory.insert().into().table(TestSQLGrammar.class)
                .columns("", "")
                .values()
                .row("", "")
                .row("", "")
                .row("", "");


        ///

        unifyBuilder = CreateFactory.create().database().ifNotExist().name("").charset("").collate("");

        unifyBuilder = CreateFactory.create().table().ifNotExist().name("")
                .column("").intType().autoIncrement()
                .column("").intType().autoIncrement()
                .charset("").extra("");

        unifyBuilder = CreateFactory.create().unique().index().name("").on().table(TestSQLGrammar.class).columns("", "");


        ///

        unifyBuilder = DropFactory.drop().database().ifExist().name("");
        unifyBuilder = DropFactory.drop().table().ifExist().table(TestSQLGrammar.class);
        unifyBuilder = DropFactory.drop().index().name("").on().table(TestSQLGrammar.class);

        ///
        unifyBuilder = AlterFactory.alter().database()
                .name("")
                .charset("").collate("");

        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class)
                .add().column("").intType().autoIncrement().after().column("");


        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class)
                .add().index().name("").column("").comment("");

        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class)
                .change().oldColumn("").newColumn("").intType().autoIncrement().after().column("");

        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class)
                .modify().column("").intType().autoIncrement().after().column("");

        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class).drop().column("");
        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class).drop().index().name("");
        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class).drop().primary().key();

        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class).rename().column().oldColumn("").to().newColumn("");
        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class).rename().index().oldColumn("").to().newColumn("");
        unifyBuilder = AlterFactory.alter().table(TestSQLGrammar.class).rename().name("");
    }
}
