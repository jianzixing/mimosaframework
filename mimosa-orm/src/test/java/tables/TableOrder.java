package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.math.BigDecimal;
import java.util.Date;

@Table
public enum TableOrder {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = long.class)
    productId,
    @Column(type = long.class)
    userId,
    @Column(type = BigDecimal.class, length = 32, scale = 2)
    orderMoney,
    @Column(length = 200)
    address,
    @Column(length = 15)
    phone,
    @Column(type = Date.class)
    createdTime,
    @Column(timeForUpdate = true)
    modifiedDate
}
