package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.math.BigDecimal;
import java.util.Date;

@Table
public enum TablePay {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = long.class)
    userId,
    @Column(type = long.class)
    orderId,
    @Column(type = short.class, defaultValue = "100")
    status,
    @Column(type = BigDecimal.class, length = 16, decimalDigits = 2)
    payMoney,
    @Column(type = Date.class)
    createdTime,
    @Column(timeForUpdate = true)
    modifiedDate
}
