package tables.b.list;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

@Table
public class AEntity implements Serializable {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    private Integer id;
    @Column(type = int.class)
    private int userId;
    @Column(type = BigDecimal.class, scale = 2)
    private BigDecimal money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
