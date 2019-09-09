package session;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionPropagationType;
import tables.TableUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class TransactionSessionTesting {
    private static SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/transaction-mimosa.xml"));
            SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
            template = new MimosaSessionTemplate();
            ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        }
    }

    @Test
    public void transForNormal() throws MimosaException, IOException, SQLException {
        System.out.println(MimosaDataSource.getDataSourceSize());
        final ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws Exception {
                template.save(object);
                transaction.rollback();
                return true;
            }
        });

        ModelObject object1 = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object1 != null) {
            throw new IllegalStateException("应该回滚的不应该保存成功!");
        }
    }

    @Test
    public void transForNormalSave() throws SQLException {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());

        final ModelObject finalObject = object;
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws Exception {
                template.save(finalObject);
                return true;
            }
        });

        object = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object == null) {
            throw new IllegalStateException("事务提交后应该保存成功!");
        }
    }

    @Test
    public void transDeleteFail() throws Exception {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());

        template.save(object);
        final ModelObject finalObject = object;
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                template.delete(TableUser.class, finalObject.getLongValue(TableUser.id));
                try {
                    transaction.rollback();
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        object = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object == null) {
            throw new IllegalStateException("删除事务失败但是还是存在!");
        }
    }

    @Test
    public void transDeleteSucc() throws SQLException {
        final ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());

        template.save(object);

        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                template.delete(TableUser.class, object.getLongValue(TableUser.id));
                return true;
            }
        });

        ModelObject object2 = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object2 != null) {
            throw new IllegalStateException("删除事务失败但是还是存在!");
        }
    }

    @Test
    public void transUpdateFail() throws TransactionException {
        final ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区123");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());

        template.save(object);

        final String c = RandomUtils.randomChineseCharacters(3, 10);
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                object.put(TableUser.address, c);
                template.update(object);
                try {
                    transaction.rollback();
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        ModelObject object2 = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object2 == null || object2.getString(TableUser.address).equals(c)) {
            throw new IllegalStateException("回滚更新但是不正确!");
        }
    }


    @Test
    public void transUpdateSucc() throws TransactionException {
        final ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区123");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());

        template.save(object);

        final String c = RandomUtils.randomChineseCharacters(3, 10);
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                object.put(TableUser.address, c);
                template.update(object);
                return true;
            }
        });

        ModelObject object2 = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object2 == null || !object2.getString(TableUser.address).equals(c)) {
            throw new IllegalStateException("更新但是不正确!");
        }
    }

    @Test
    public void testSupports() {
        final ModelObject object1 = new ModelObject(TableUser.class);
        object1.put(TableUser.userName, "yangankang");
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区123");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());

        try {
            template.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean invoke(Transaction transaction) throws TransactionException {
                    template.save(object1);

                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            throw new Exception("");
                        }
                    }, TransactionPropagationType.PROPAGATION_SUPPORTS);
                    return true;
                }
            });
        } catch (Exception e) {
        }

        ModelObject object2 = template.get(TableUser.class, object1.getLongValue(TableUser.id));
        if (object2 != null) {
            throw new IllegalStateException("Supports传播级别失败!");
        }

        final ModelObject object3 = (ModelObject) object1.clone();
        try {
            template.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean invoke(Transaction transaction) throws TransactionException {
                    template.save(object3);

                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            throw new Exception("");
                        }
                    }, TransactionPropagationType.PROPAGATION_SUPPORTS);
                    return true;
                }
            }, TransactionPropagationType.PROPAGATION_SUPPORTS);
        } catch (Exception e) {
        }
        ModelObject object4 = template.get(TableUser.class, object3.getLongValue(TableUser.id));
        if (object4 == null) {
            throw new IllegalStateException("Supports传播级别失败!");
        }
    }


    @Test
    public void testMandatory() {
        final ModelObject object1 = new ModelObject(TableUser.class);
        object1.put(TableUser.userName, "yangankang");
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区123");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());

        try {
            template.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean invoke(Transaction transaction) throws TransactionException {
                    template.save(object1);

                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            throw new Exception("");
                        }
                    }, TransactionPropagationType.PROPAGATION_MANDATORY);
                    return true;
                }
            });
        } catch (Exception e) {
        }

        ModelObject object2 = template.get(TableUser.class, object1.getLongValue(TableUser.id));
        if (object2 != null) {
            throw new IllegalStateException("Mandatory传播级别失败!");
        }

        final ModelObject object3 = (ModelObject) object1.clone();
        boolean isThrow = false;
        try {
            template.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean invoke(Transaction transaction) {
                    template.save(object3);
                    return true;
                }
            }, TransactionPropagationType.PROPAGATION_MANDATORY);
        } catch (Exception e) {
            isThrow = true;
        }
        if (!isThrow) {
            throw new IllegalStateException("Mandatory传播级别失败!");
        }
    }

    @Test
    public void testRequiresNew() {
        final ModelObject object1 = new ModelObject(TableUser.class);
        object1.put(TableUser.userName, "yangankang");
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区123");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());

        final ModelObject object2 = (ModelObject) object1.clone();
        try {
            template.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean invoke(Transaction transaction) throws TransactionException {
                    template.save(object1);
                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            template.save(object2);
                            return true;
                        }
                    }, TransactionPropagationType.PROPAGATION_REQUIRES_NEW);
                    throw new RuntimeException("");
                }
            }, TransactionPropagationType.PROPAGATION_REQUIRES_NEW);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (TransactionException e) {
            e.printStackTrace();
        }

        ModelObject object3 = template.get(TableUser.class, object1.getLongValue(TableUser.id));
        if (object3 != null) {
            throw new IllegalStateException("RequiresNew传播级别失败!");
        }
        ModelObject object4 = template.get(TableUser.class, object2.getLongValue(TableUser.id));
        if (object4 == null) {
            throw new IllegalStateException("RequiresNew传播级别失败!");
        }
    }

    @Test
    public void testNotSupported() {
        final ModelObject object1 = new ModelObject(TableUser.class);
        object1.put(TableUser.userName, "yangankang");
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区123");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());

        final ModelObject object2 = (ModelObject) object1.clone();
        try {
            template.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean invoke(Transaction transaction) throws TransactionException {
                    template.save(object1);
                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            template.save(object2);
                            throw new RuntimeException("");
                        }
                    }, TransactionPropagationType.PROPAGATION_NOT_SUPPORTED);
                    return true;
                }
            });
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (TransactionException e) {
            e.printStackTrace();
        }

        ModelObject object3 = template.get(TableUser.class, object1.getLongValue(TableUser.id));
        if (object3 != null) {
            throw new IllegalStateException("NotSupported传播级别失败!");
        }
        ModelObject object4 = template.get(TableUser.class, object2.getLongValue(TableUser.id));
        if (object4 == null) {
            throw new IllegalStateException("NotSupported传播级别失败!");
        }
    }


    @Test
    public void testNever() throws TransactionException {
        final ModelObject object1 = new ModelObject(TableUser.class);
        object1.put(TableUser.userName, "yangankang");
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区123");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());

        final ModelObject object2 = (ModelObject) object1.clone();
        final ModelObject object9 = (ModelObject) object1.clone();
        final boolean[] is = {false};
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                template.save(object1);
                try {
                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            template.save(object2);
                            return true;
                        }
                    }, TransactionPropagationType.PROPAGATION_NEVER);
                } catch (Exception e) {
                    is[0] = true;
                    System.out.println(e.getMessage());
                }
                return true;
            }
        });

        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws Exception {
                object9.remove(TableUser.id);
                template.save(object9);
                return true;
            }
        }, TransactionPropagationType.PROPAGATION_NEVER);

        ModelObject object3 = template.get(TableUser.class, object1.getLongValue(TableUser.id));
        if (object3 == null) {
            throw new IllegalStateException("Never传播级别失败!");
        }
        ModelObject object4 = template.get(TableUser.class, object2.getLongValue(TableUser.id));
        if (object4 != null) {
            throw new IllegalStateException("Never传播级别失败!");
        }
        if (!is[0]) {
            throw new IllegalStateException("Never传播级别失败!");
        }
    }

    @Test
    public void testNested() throws TransactionException {
        final ModelObject object1 = new ModelObject(TableUser.class);
        object1.put(TableUser.userName, "yangankang");
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区123");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());

        final ModelObject object2 = (ModelObject) object1.clone();
        final ModelObject object6 = (ModelObject) object1.clone();
        final boolean[] is = {false};
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                template.save(object1);
                try {
                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            template.save(object6);
                            return true;
                        }
                    }, TransactionPropagationType.PROPAGATION_NESTED);

                    template.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean invoke(Transaction transaction) throws Exception {
                            template.save(object2);
                            throw new RuntimeException("");
                        }
                    }, TransactionPropagationType.PROPAGATION_NESTED);
                } catch (RuntimeException e) {
                    is[0] = true;
                    System.out.println(e.getMessage());
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        ModelObject object3 = template.get(TableUser.class, object1.getLongValue(TableUser.id));
        if (object3 == null) {
            throw new IllegalStateException("Nested传播级别失败!");
        }
        ModelObject object4 = template.get(TableUser.class, object2.getLongValue(TableUser.id));
        if (object4 != null) {
            throw new IllegalStateException("Nested传播级别失败!");
        }
        ModelObject object5 = template.get(TableUser.class, object6.getLongValue(TableUser.id));
        if (object5 == null) {
            throw new IllegalStateException("Nested传播级别失败!");
        }
    }

    @Test
    public void test2() throws TransactionException {
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws TransactionException {
                ModelObject object1 = new ModelObject(TableUser.class);
                object1.put(TableUser.userName, "yangankang");
                object1.put(TableUser.password, "123456");
                object1.put(TableUser.realName, "杨安康");
                object1.put(TableUser.address, "北京朝阳区123");
                object1.put(TableUser.age, 25);
                object1.put(TableUser.level, 10);
                object1.put(TableUser.createdTime, new Date());
                template.save(object1);

                template.execute(new TransactionCallback<Boolean>() {
                    @Override
                    public Boolean invoke(Transaction transaction) throws Exception {
                        ModelObject object2 = new ModelObject(TableUser.class);
                        object2.put(TableUser.userName, "yangankang");
                        object2.put(TableUser.password, "123456");
                        object2.put(TableUser.realName, "杨安康");
                        object2.put(TableUser.address, "北京朝阳区123");
                        object2.put(TableUser.age, 25);
                        object2.put(TableUser.level, 10);
                        object2.put(TableUser.createdTime, new Date());
                        template.save(object2);

                        template.execute(new TransactionCallback<Boolean>() {
                            @Override
                            public Boolean invoke(Transaction transaction) throws Exception {
                                ModelObject object3 = new ModelObject(TableUser.class);
                                object3.put(TableUser.userName, "yangankang");
                                object3.put(TableUser.password, "123456");
                                object3.put(TableUser.realName, "杨安康");
                                object3.put(TableUser.address, "北京朝阳区123");
                                object3.put(TableUser.age, 25);
                                object3.put(TableUser.level, 10);
                                object3.put(TableUser.createdTime, new Date());
                                template.save(object3);

                                template.execute(new TransactionCallback<Boolean>() {
                                    @Override
                                    public Boolean invoke(Transaction transaction) throws Exception {
                                        ModelObject object4 = new ModelObject(TableUser.class);
                                        object4.put(TableUser.userName, "yangankang");
                                        object4.put(TableUser.password, "123456");
                                        object4.put(TableUser.realName, "杨安康");
                                        object4.put(TableUser.address, "北京朝阳区123");
                                        object4.put(TableUser.age, 25);
                                        object4.put(TableUser.level, 10);
                                        object4.put(TableUser.createdTime, new Date());
                                        template.save(object4);

                                        template.execute(new TransactionCallback<Boolean>() {
                                            @Override
                                            public Boolean invoke(Transaction transaction) throws Exception {
                                                ModelObject object4 = new ModelObject(TableUser.class);
                                                object4.put(TableUser.userName, "yangankang");
                                                object4.put(TableUser.password, "123456");
                                                object4.put(TableUser.realName, "杨安康");
                                                object4.put(TableUser.address, "北京朝阳区123");
                                                object4.put(TableUser.age, 25);
                                                object4.put(TableUser.level, 10);
                                                object4.put(TableUser.createdTime, new Date());
                                                template.save(object4);

                                                template.execute(new TransactionCallback<Boolean>() {
                                                    @Override
                                                    public Boolean invoke(Transaction transaction) throws Exception {
                                                        ModelObject object4 = new ModelObject(TableUser.class);
                                                        object4.put(TableUser.userName, "yangankang");
                                                        object4.put(TableUser.password, "123456");
                                                        object4.put(TableUser.realName, "杨安康");
                                                        object4.put(TableUser.address, "北京朝阳区123");
                                                        object4.put(TableUser.age, 25);
                                                        object4.put(TableUser.level, 10);
                                                        object4.put(TableUser.createdTime, new Date());
                                                        template.save(object4);
                                                        return true;
                                                    }
                                                }, TransactionPropagationType.PROPAGATION_NESTED);
                                                return true;
                                            }
                                        }, TransactionPropagationType.PROPAGATION_NOT_SUPPORTED);
                                        return true;
                                    }
                                }, TransactionPropagationType.PROPAGATION_REQUIRES_NEW);
                                return true;
                            }
                        }, TransactionPropagationType.PROPAGATION_MANDATORY);
                        return true;
                    }
                }, TransactionPropagationType.PROPAGATION_SUPPORTS);
                return true;
            }
        });
    }
}
