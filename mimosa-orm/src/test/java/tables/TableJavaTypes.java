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
    @Column(type = Blob.class)
    blob,
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
