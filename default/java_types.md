# 字段类型列表

映射类支持的字段类型列表：

|  类型   | 说明  |  版本支持 |
|  ----  |  ----  |  ----  |
| int.class  | 整型数据 | all |
| Integer.class  | 整型数据 | all |
| long.class  | 长整型数据 | all |
| Long.class  | 长整型数据 | all |
| String.class  | 变长字符串(默认长度255) | all |
| Date.class  | 日期(理论上包含年月日时分秒) | all |
| double.class  | 双精度浮点型数据 | all |
| Double.class  | 双精度浮点型数据 | all |
| short.class  | 短整型数据 | all |
| Short.class  | 短整型数据 | all |
| float.class  | 单精度浮点型数据 | all |
| Float.class  | 单精度浮点型数据 | all |
| char.class  | 字符类型数据(默认长度255) | all |
| Text.class  | 文本类型数据(不同数据库长度不定) | all |
| MediumText.class  | 大文本数据类型(不同数据库不一致) | all |
| BigDecimal.class  | 小数类型数据(length设置整型长度，decimalDigits设置小数长度) | all |
| byte.class  | 字节数据 | 3.3.9+ |
| Byte.class  | 字节数据 | 3.3.9+ |
| boolean.class  | 布尔数据(1表示true，0表示false) | all |
| Boolean.class  | 布尔数据(1表示true，0表示false) | all |
| Clob.class  | 大文本数据(某些数据库和Text一致) | 3.3.9+ |
| Timestamp.class  | 时间戳类型 | all |


PS：每种类型对应数据库中的类型也有区别的，不同数据库支持的类型都有差异，为了尽量
满足以上类型需要，类型映射会有使用类型的替代，使用时请注意。


```java
package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.platform.MediumText;
import org.mimosaframework.orm.platform.Text;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Date;

@Table
public enum TableJavaTypes {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = Text.class)
    text,
    @Column(type = MediumText.class)
    mediumText,
    @Column(type = double.class)
    doubleType,
    @Column(type = BigDecimal.class, length = 20)
    decimal,
    @Column(type = String.class)
    stringType,
    @Column(type = char.class)
    charType,
    @Column(type = Date.class)
    date,
    @Column(type = Clob.class)
    clob,
    @Column(type = short.class)
    shortType,
    @Column(type = byte.class)
    byteType,
    @Column(type = long.class)
    longType,
    @Column(type = float.class)
    floatType,
    @Column(type = boolean.class)
    booleanType,
    @Column(type = Timestamp.class)
    timestamp
}

```


## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
