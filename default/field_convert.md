# 表字段转换格式

默认情况下映射类字段和表字段是一致的，如果需要将字段格式转换则只需要在配置文件中配置convertType属性。

* H2U：该配置是将映射表的驼峰命名转换成下划线命名。

如果需要自定义字段转换器，则实现MappingNamedConvert接口即可，然后在配置文件中配置convert属性。

```java
import org.mimosaframework.orm.convert.MappingNamedConvert;

public class CustomMappingNamedConvert implements MappingNamedConvert {

    /**
     * 正向转换，比如将驼峰命名转换成下划线命名
     *
     * @param s
     * @return
     */
    public String convert(String s) {
        return null;
    }

    /**
     * 逆向转换，比如将下划线命名转换成驼峰命名
     *
     * @param s
     * @return
     */
    public String reverse(String s) {
        return null;
    }
}
```

由于需要正向和逆向的原因，在数据库字段命名时需要注意，连续的大写会导致命名很难看。
比如 USERName 转换后变成 u_s_e_r_name，所以在命名时尽量的驼峰格式。



## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
