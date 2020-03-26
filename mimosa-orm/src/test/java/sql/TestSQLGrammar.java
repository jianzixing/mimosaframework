package sql;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.alter.AlterFactory;
import org.mimosaframework.orm.sql.create.Columns;
import org.mimosaframework.orm.sql.create.CreateFactory;
import org.mimosaframework.orm.sql.delete.DeleteFactory;
import org.mimosaframework.orm.sql.drop.DropFactory;
import org.mimosaframework.orm.sql.insert.InsertFactory;
import org.mimosaframework.orm.sql.select.SelectFactory;
import org.mimosaframework.orm.sql.update.UpdateFactory;

public class TestSQLGrammar {
    public static void main(String[] args) {
        UnifyBuilder unifyBuilder = null;

        unifyBuilder = DeleteFactory.delete()
                .table(TestSQLGrammar.class)
                .from()
                .table(TestSQLGrammar.class, "t1")
                .table(TestSQLGrammar.class, "t2")
                .where()
                .column("").eq().value("");

        unifyBuilder = DeleteFactory.delete().from().table(TestSQLGrammar.class)
                .where()
                .column("").gt().value("")
                .and()
                .wrapper(null).and().wrapper(null).and()
                .column("").in().value("")
                .orderBy()
                .column("").asc()
                .column("").desc()
                .column("").asc()
                .limit(0, 10);

        unifyBuilder = DeleteFactory.delete().from()
                .table(TestSQLGrammar.class)
                .using()
                .table(TestSQLGrammar.class, "t1")
                .table(TestSQLGrammar.class, "t2")
                .where()
                .column("").eq().value("");

        unifyBuilder = DeleteFactory.delete()
                .table("t1")
                .from()
                .table(TestSQLGrammar.class, "t1")
                .table(TestSQLGrammar.class, "t2")
                .where()
                .column("").eq().value("");

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
                .column("").isNull("")
                .and().wrapper(Wrapper.build().column("").eq().value("").and().wrapper(null));

        unifyBuilder = SelectFactory.select().all().from()
                .table(TestSQLGrammar.class, "")
                .left().join().table(TestSQLGrammar.class, "").on().column("").eq().value("").and().wrapper(null).and().column("").eq().value("")
                .inner().join().table(TestSQLGrammar.class).on().column("").eq().value("")
                .where().column("").eq().value("")
                .groupBy().column("").column("")
                .having().count(new FieldItem(1)).eq().value("")
                .orderBy()
                .column("").asc()
                .column("").desc()
                .limit(0, 10);


        ///

        unifyBuilder = UpdateFactory.update()
                .table(TestSQLGrammar.class, "t1")
                .table(TestSQLGrammar.class, "t2")
                .set()
                .column("").eq().value("")
                .column("").eq().value("")
                .where().column("").eq().value("")
                .orderBy()
                .column("").asc()
                .column("").desc()
                .limit(0, 10);


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

        unifyBuilder = CreateFactory.create().table().ifNotExist().name("").columns(
                Columns.column("id").intType().primary().key().autoIncrement().not().nullable()
        ).charset("").extra("");

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
                .add().column("").intType().autoIncrement().after().column("")
                .split()
                .drop().column("");


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
