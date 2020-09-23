# ID生成策略

在建表时，如果需要自动生成字段值，则可以使用ID生成策略，内置的生成策略有两个。

* AutoIncrementStrategy ：数据库的自增策略，一个表中只允许有一个自增策略。**自增主键新增保存对象时不允许存在(如果存在会自动移除)**
* UUIDStrategy ：UUID生成策略，会生成32位小写字符串。

如果以上的内置策略无法满足，则可以自定义ID生成策略。自定义生成策略需要实现IDStrategy
接口。比如UUIDStrategy

**必须是无构造参数类**

```java
public class UUIDStrategy implements IDStrategy {
    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
```

使用时在字段上配置strategy即可

```java
@Column(pk = true, type = String.class, strategy = UUIDStrategy.class)
```

如果直接使用自定义的生成策略不需要配置，如果需要自定义策略中有配置参数，则可以在配置文件中
配置好参数。

```xml
<strategies>
    <strategy class="xmlcontext.TestStrategy">
        <property name="pm" value="177.0.0.1"/>
    </strategy>
</strategies>
```

或者使用spring

```xml
<property name="strategies">
    <bean class="xmlcontext.TestStrategy">
        <property name="pm" value="xxx"/>
    </bean>
</property>
```

以上配置不是必须的，如果不使用以上配置实例化ID生成策略，那么第一次添加时会自动初始化实例
这种初始化无法初始化参数。

* StrategyWrapper带有一些表信息的包装类。
* Session当前的数据库执行Session。

## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
