package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Index;
import org.mimosaframework.orm.annotation.IndexItem;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.math.BigDecimal;
import java.util.Date;

@Table
@Index({
        @IndexItem(indexName = "g1", columns = {"name", "price"}),
        @IndexItem(indexName = "g2", unique = true, columns = {"name", "price"})
})
public enum TableProduct {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(length = 200)
    name,
    @Column(type = BigDecimal.class, length = 16, scale = 2, defaultValue = "0.01")
    price,
    @Column(type = int.class, defaultValue = "1")
    amount,
    @Column(type = Date.class)
    createdTime,
    @Column(timeForUpdate = true)
    modifiedDate
}
