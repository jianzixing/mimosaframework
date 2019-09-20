# 数据库事务

使用数据库事务可以直接用SessionTemplate上的方法即可。

我们建议如果您使用了Spring的事务管理那么就最好不要使用template上的事务管理。

```java
template.execute(new TransactionCallback<Boolean>() {
    public Boolean invoke(Transaction transaction) throws Exception {
        ModelObject object = new ModelObject(TableUser.class);
        template.save(object);
        return true;
    }
});
```

如果需要自己控制回滚则可以这么写


```java
Transaction transaction = null;
try {
    transaction = template.beginTransaction();
    ModelObject object = new ModelObject(TableUser.class);
    ...
    template.save(object);
    transaction.commit();
} catch (Exception e) {
    if (transaction != null) {
        transaction.rollback();
    }
}
```

## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
