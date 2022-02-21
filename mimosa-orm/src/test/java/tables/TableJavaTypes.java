package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Table
public enum TableJavaTypes {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = int.class)
    intType,
    @Column(type = String.class)
    varchar,
    @Column(type = char.class)
    charType,
    @Column(type = byte.class)
    tinyint,
    @Column(type = short.class)
    smallint,
    @Column(type = long.class)
    bigint,
    @Column(type = float.class)
    floatType,
    @Column(type = double.class)
    doubleType,
    @Column(type = BigDecimal.class, scale = 2)
    decimal,
    @Column(type = boolean.class)
    booleanType,
    @Column(type = java.sql.Date.class)
    date,
    @Column(type = Time.class)
    time,
    @Column(type = Date.class)
    datetime,
    @Column(type = Timestamp.class)
    timestamp,
    @Column(type = SupportBlob.class)
    blob,
    @Column(type = SupportMediumBlob.class)
    mediumblob,
    @Column(type = SupportLongBlob.class)
    longblob,
    @Column(type = SupportText.class)
    text,
    @Column(type = SupportJSON.class)
    json,
    @Column(type = SupportMediumText.class)
    mediumtext,
    @Column(type = SupportLongText.class)
    longtext
}
