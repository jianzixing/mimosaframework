package program.service;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;
import tables.TablePay;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

public class TransactionSessionServiceTesting {

    public static void transForNormal(final SessionTemplate template) throws MimosaException, IOException, SQLException {

        ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.status, 10);
        object.put(TablePay.payMoney, "10.34");
        object.put(TablePay.userId, 0);
        object.put(TablePay.createdTime, new Date());

        final ModelObject finalObject = object;
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws Exception {
                template.save(finalObject);
                transaction.rollback();
                return true;
            }
        });

        object = template.get(TablePay.class, object.getLongValue(TablePay.id));
        if (object != null) {
            throw new IllegalStateException("应该回滚的不应该保存成功!");
        }
    }

    public static void transForNormalSave(final SessionTemplate template) throws SQLException {
        ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.status, 10);
        object.put(TablePay.payMoney, "10.34");
        object.put(TablePay.userId, 0);
        object.put(TablePay.createdTime, new Date());
        object.put(TablePay.createdTime, new Date());

        final ModelObject finalObject = object;
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws Exception {
                template.save(finalObject);
                return true;
            }
        });


        object = template.get(TablePay.class, object.getLongValue(TablePay.id));
        if (object == null) {
            throw new IllegalStateException("事务提交后应该保存成功!");
        }
    }

    public static void transDeleteFail(final SessionTemplate template) throws Exception {
        ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.status, 10);
        object.put(TablePay.payMoney, "10.34");
        object.put(TablePay.userId, 0);
        object.put(TablePay.createdTime, new Date());
        object.put(TablePay.createdTime, new Date());

        template.save(object);
        final ModelObject finalObject = object;
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                template.delete(TablePay.class, finalObject.getLongValue(TablePay.id));
                try {
                    transaction.rollback();
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        object = template.get(TablePay.class, object.getLongValue(TablePay.id));
        if (object == null) {
            throw new IllegalStateException("删除事务失败但是还是存在!");
        }
    }

    public static void transDeleteSucc(final SessionTemplate template) throws SQLException {
        final ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.status, 10);
        object.put(TablePay.payMoney, "10.34");
        object.put(TablePay.userId, 0);
        object.put(TablePay.createdTime, new Date());
        object.put(TablePay.createdTime, new Date());

        template.save(object);

        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                template.delete(TablePay.class, object.getLongValue(TablePay.id));
                return true;
            }
        });

        ModelObject object2 = template.get(TablePay.class, object.getLongValue(TablePay.id));
        if (object2 != null) {
            throw new IllegalStateException("删除事务失败但是还是存在!");
        }
    }

    public static void transUpdateFail(final SessionTemplate template) throws TransactionException {
        final ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.status, 10);
        object.put(TablePay.payMoney, "10.34");
        object.put(TablePay.userId, 0);
        object.put(TablePay.createdTime, new Date());

        template.save(object);

        final BigDecimal c = new BigDecimal("10.34").add(new BigDecimal(RandomUtils.randomDecimalNumber(3, 100, 2)));
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                object.put(TablePay.payMoney, c);
                template.update(object);
                try {
                    transaction.rollback();
                } catch (TransactionException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        ModelObject object2 = template.get(TablePay.class, object.getLongValue(TablePay.id));
        if (object2 == null || object2.getBigDecimal(TablePay.payMoney).equals(c)) {
            System.out.println(object2.getBigDecimal(TablePay.payMoney) + "   " + c);
            throw new IllegalStateException("回滚更新但是不正确!");
        }
    }

    public static void transUpdateSucc(final SessionTemplate template) throws TransactionException {
        final ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.status, 10);
        object.put(TablePay.payMoney, "10.34");
        object.put(TablePay.userId, 0);
        object.put(TablePay.createdTime, new Date());

        template.save(object);

        final BigDecimal c = new BigDecimal("10.34").add(new BigDecimal(RandomUtils.randomDecimalNumber(3, 100, 2)));
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) {
                object.put(TablePay.payMoney, c);
                template.update(object);
                return true;
            }
        });

        ModelObject object2 = template.get(TablePay.class, object.getLongValue(TablePay.id));
        if (object2 == null || !object2.getBigDecimal(TablePay.payMoney).equals(c)) {
            System.out.println(object2.getBigDecimal(TablePay.payMoney) + "   " + c);
            throw new IllegalStateException("更新但是不正确!");
        }
    }

    public static void testSupports(final SessionTemplate template) {
        final ModelObject object1 = new ModelObject(TablePay.class);
        object1.put(TablePay.status, 10);
        object1.put(TablePay.payMoney, "10.34");
        object1.put(TablePay.userId, 0);
        object1.put(TablePay.createdTime, new Date());

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

        ModelObject object2 = template.get(TablePay.class, object1.getLongValue(TablePay.id));
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
        ModelObject object4 = template.get(TablePay.class, object3.getLongValue(TablePay.id));
        if (object4 == null) {
            throw new IllegalStateException("Supports传播级别失败!");
        }
    }

    public static void testMandatory(final SessionTemplate template) {
        final ModelObject object1 = new ModelObject(TablePay.class);
        object1.put(TablePay.status, 10);
        object1.put(TablePay.payMoney, "10.34");
        object1.put(TablePay.userId, 0);
        object1.put(TablePay.createdTime, new Date());

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

        ModelObject object2 = template.get(TablePay.class, object1.getLongValue(TablePay.id));
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

    public static void testRequiresNew(final SessionTemplate template) {
        final ModelObject object1 = new ModelObject(TablePay.class);
        object1.put(TablePay.status, 10);
        object1.put(TablePay.payMoney, "10.34");
        object1.put(TablePay.userId, 0);
        object1.put(TablePay.createdTime, new Date());

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

                    throw new RuntimeException("?");
                }
            }, TransactionPropagationType.PROPAGATION_REQUIRES_NEW);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

        ModelObject object3 = template.get(TablePay.class, object1.getLongValue(TablePay.id));
        if (object3 != null) {
            throw new IllegalStateException("RequiresNew传播级别失败!");
        }
        ModelObject object4 = template.get(TablePay.class, object2.getLongValue(TablePay.id));
        if (object4 == null) {
            throw new IllegalStateException("RequiresNew传播级别失败!");
        }
    }

    public static void testNotSupported(final SessionTemplate template) {
        final ModelObject object1 = new ModelObject(TablePay.class);
        object1.put(TablePay.status, 10);
        object1.put(TablePay.payMoney, "10.34");
        object1.put(TablePay.userId, 0);
        object1.put(TablePay.createdTime, new Date());

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
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }

        ModelObject object3 = template.get(TablePay.class, object1.getLongValue(TablePay.id));
        if (object3 != null) {
            throw new IllegalStateException("NotSupported传播级别失败!");
        }
        ModelObject object4 = template.get(TablePay.class, object2.getLongValue(TablePay.id));
        if (object4 == null) {
            throw new IllegalStateException("NotSupported传播级别失败!");
        }
    }

    public static void testNever(final SessionTemplate template) throws TransactionException {
        final ModelObject object1 = new ModelObject(TablePay.class);
        object1.put(TablePay.status, 10);
        object1.put(TablePay.payMoney, "10.34");
        object1.put(TablePay.userId, 0);
        object1.put(TablePay.createdTime, new Date());

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
//                    System.out.println(e.getMessage());
                }
                return true;
            }
        });

        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws Exception {
                object9.remove(TablePay.id);
                template.save(object9);
                return true;
            }
        }, TransactionPropagationType.PROPAGATION_NEVER);

        ModelObject object3 = template.get(TablePay.class, object1.getLongValue(TablePay.id));
        if (object3 == null) {
            throw new IllegalStateException("Never传播级别失败!");
        }
        ModelObject object4 = template.get(TablePay.class, object2.getLongValue(TablePay.id));
        if (object4 != null) {
            throw new IllegalStateException("Never传播级别失败!");
        }
        if (!is[0]) {
            throw new IllegalStateException("Never传播级别失败!");
        }
    }

    public static void testNested(final SessionTemplate template) throws TransactionException {
        final ModelObject object1 = new ModelObject(TablePay.class);
        object1.put(TablePay.status, 10);
        object1.put(TablePay.payMoney, "10.34");
        object1.put(TablePay.userId, 0);
        object1.put(TablePay.createdTime, new Date());

        final ModelObject object2 = (ModelObject) object1.clone();
        final ModelObject object6 = (ModelObject) object1.clone();
        final boolean[] is = {false};
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws TransactionException {
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
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw new TransactionException(e);
                    }
                    is[0] = true;
//                    System.out.println(e.getMessage());
                }
                return true;
            }
        }, TransactionIsolationType.REPEATABLE_READ);

        ModelObject object3 = template.get(TablePay.class, object1.getLongValue(TablePay.id));
        if (object3 == null) {
            throw new IllegalStateException("Nested传播级别失败!");
        }
        ModelObject object4 = template.get(TablePay.class, object2.getLongValue(TablePay.id));
        if (object4 != null) {
            throw new IllegalStateException("Nested传播级别失败!");
        }
        ModelObject object5 = template.get(TablePay.class, object6.getLongValue(TablePay.id));
        if (object5 == null) {
            throw new IllegalStateException("Nested传播级别失败!");
        }
    }

    public static void test2(final SessionTemplate template) throws TransactionException {
        template.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean invoke(Transaction transaction) throws TransactionException {
                ModelObject object1 = new ModelObject(TablePay.class);
                object1.put(TablePay.status, 10);
                object1.put(TablePay.payMoney, "10.34");
                object1.put(TablePay.userId, 0);
                object1.put(TablePay.createdTime, new Date());
                template.save(object1);

                template.execute(new TransactionCallback<Boolean>() {
                    @Override
                    public Boolean invoke(Transaction transaction) throws Exception {
                        ModelObject object2 = new ModelObject(TablePay.class);
                        object2.put(TablePay.status, 10);
                        object2.put(TablePay.payMoney, "10.34");
                        object2.put(TablePay.userId, 0);
                        object2.put(TablePay.createdTime, new Date());
                        template.save(object2);

                        template.execute(new TransactionCallback<Boolean>() {
                            @Override
                            public Boolean invoke(Transaction transaction) throws Exception {
                                ModelObject object3 = new ModelObject(TablePay.class);
                                object3.put(TablePay.status, 10);
                                object3.put(TablePay.payMoney, "10.34");
                                object3.put(TablePay.userId, 0);
                                object3.put(TablePay.createdTime, new Date());
                                template.save(object3);

                                template.execute(new TransactionCallback<Boolean>() {
                                    @Override
                                    public Boolean invoke(Transaction transaction) throws Exception {
                                        ModelObject object4 = new ModelObject(TablePay.class);
                                        object4.put(TablePay.status, 10);
                                        object4.put(TablePay.payMoney, "10.34");
                                        object4.put(TablePay.userId, 0);
                                        object4.put(TablePay.createdTime, new Date());
                                        template.save(object4);

                                        template.execute(new TransactionCallback<Boolean>() {
                                            @Override
                                            public Boolean invoke(Transaction transaction) throws Exception {
                                                ModelObject object4 = new ModelObject(TablePay.class);
                                                object4.put(TablePay.status, 10);
                                                object4.put(TablePay.payMoney, "10.34");
                                                object4.put(TablePay.userId, 0);
                                                object4.put(TablePay.createdTime, new Date());
                                                template.save(object4);

                                                template.execute(new TransactionCallback<Boolean>() {
                                                    @Override
                                                    public Boolean invoke(Transaction transaction) throws Exception {
                                                        ModelObject object4 = new ModelObject(TablePay.class);
                                                        object4.put(TablePay.status, 10);
                                                        object4.put(TablePay.payMoney, "10.34");
                                                        object4.put(TablePay.userId, 0);
                                                        object4.put(TablePay.createdTime, new Date());
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
