package comprehensive;


import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.utils.DatabaseType;
import tables.TableOrder;
import tables.TableUser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class RunBaseSession {
    private SessionTemplate template;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            template = RunDataSourceBuilder.currTemplate();
        }
    }

    @Test
    public void save() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);
        System.out.println(user);
    }

    @Test
    public void saveAndUpdate() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put("id", 3);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.saveOrUpdate(user);
        System.out.println(user);
    }

    @Test
    public void update() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);

        int id = user.getIntValue(TableUser.id);
        ModelObject update = new ModelObject(TableUser.class);
        update.put(TableUser.id, id);
        update.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        update.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.update(update);

        template.update(Criteria.update(TableUser.class)
                .set(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30))
                .eq(TableUser.id, id));
    }


    @Test
    public void delete() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);

        int id = user.getIntValue(TableUser.id);
        ModelObject update = new ModelObject(TableUser.class);
        update.put(TableUser.id, id);
        update.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        update.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.delete(update);

        template.delete(Criteria.delete(TableUser.class).eq(TableUser.id, id));
    }

    @Test
    public void get() {
        ModelObject user = new ModelObject(TableUser.class);
        user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.put(TableUser.password, RandomUtils.randomIgnoreCaseAlphanumeric(30));
        template.save(user);

        int id = user.getIntValue(TableUser.id);
        user = template.get(Criteria.query(TableUser.class).eq(TableUser.id, id));
        System.out.println(user);
        user = template.get(TableUser.class, id);
        System.out.println(user);
    }

    @Test
    public void list() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .limit(0, 10));
        System.out.println(objects);

        objects = template.list(Criteria.query(TableUser.class)
                .fields(TableUser.id, TableUser.level)
                .excludes(TableUser.level).limit(0, 10));
        System.out.println(objects);
    }

    @Test
    public void queryOr() {
        template.list(Criteria.query(TableUser.class)
                .linked(
                        Criteria.linked().eq(TableUser.userName, "1").or().eq(TableUser.userName, "2")
                )
                .or()
                .linked(
                        Criteria.linked().eq(TableUser.userName, "3").or().eq(TableUser.userName, "4")
                )
        );
    }

    @Test
    public void count() {
        long count = template.count(Criteria.query(TableUser.class)
                .limit(0, 10));
        System.out.println(count);
    }

    @Test
    public void paging() {
        Paging paging = template.paging(Criteria.query(TableUser.class)
                .limit(0, 10));
        System.out.println(paging);
    }

    @Test
    public void getZipperTable() {
        ZipperTable<ModelObject> zipperTable = template.getZipperTable(TableOrder.class);
        Iterator iterator = zipperTable.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void calculate() {
        AutoResult objects = template.calculate(Criteria.fun(TableUser.class)
                .addFunction(BasicFunction.AVG, TableUser.level, "level")
                .lt(TableUser.id, 1000));
        System.out.println(objects.getObjects());

        objects = template.calculate(Criteria.fun(TableUser.class)
                .addFunction(BasicFunction.COUNT, TableUser.id, "count")
                .having(new HavingField(BasicFunction.COUNT).gt(TableUser.level, 2))
                .lt(TableUser.id, 1000));
        System.out.println(objects.getObjects());
    }

//    @Test
//    public void getAutonomously() throws Exception {
//        AutoResult result = template.getAutonomously(SQLAutonomously.select().all().from().table(TableOrder.class).autonomously());
//        System.out.println(result.getObjects());
//    }

    @Test
    public void getDataSourceNames() {
        List<DataSourceTableName> tableNames = template.getDataSourceNames(TableUser.class);
        System.out.println(tableNames);
    }

    @Test
    public void subSelf() {
        template.update(Criteria.update(TableUser.class)
                .addSelf(TableUser.age)
                .eq(TableUser.id, 1));
        template.update(Criteria.update(TableUser.class)
                .subSelf(TableUser.age)
                .eq(TableUser.id, 1));
    }

    @Test
    public void exists() {
        List list = template.list(Criteria.query(TableUser.class)
                .exists(Criteria.query(TableUser.class).eq(TableUser.age, 10)));
        System.out.println(list);
    }

    @Test
    public void close() throws IOException {

    }
}
