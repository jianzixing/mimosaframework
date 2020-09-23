# 表字段转换格式

默认情况下映射类字段和表字段是一致的，如果需要将字段格式转换则只需要在配置文件中配置convertType属性。

* DEFAULT：默认的转换器，表名称会将第一个小写，字段名称不做任何处理。
* H2U：该配置是将映射表的驼峰命名转换成下划线命名。

如果需要自定义字段转换器，则实现MappingNamedConvert接口即可，然后在配置文件中配置convert属性。
convert方法有两个参数，第一个是要转换的名称，第二个是当前名称的类型的枚举

* TABLE_NAME：表示是表名称。
* FIELD_NAME：表示是自动名称。

字段转换只在有映射类时有效，执行自定义SQL或者使用Mapper时默认返回的是数据库原本的字段名称。
如果需要转换，使用AutoResult#setTableClass方法就可以转换成映射类中的字段(如果不存在)
不做转换。

```java
import org.mimosaframework.orm.convert.MappingNamedConvert;

public class DefaultMappingNamedConvert implements MappingNamedConvert {
    public String convert(String name, ConvertType type) {
        if (type.equals(ConvertType.TABLE_NAME)) {
            if (name.length() > 1) {
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
            } else {
                name = name.toLowerCase();
            }
        }
        return name;
    }
}
```

实现后再配置文件中配置好就会使用自定义配置的转换器。

xml配置方式
```xml
<!--不带参数实例化-->
<!--<convert class="xmlcontext.TestConvert"/>-->

<!--带参数实例化-->
<convert class="xmlcontext.TestConvert">
    <property name="pm">abc</property>
</convert>
```


Spring配置方式

```xml
<property name="convert">
    <bean class="xmlcontext.TestConvert">
        <property name="pm" value="xxx"/>
    </bean>
</property>
```

## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
