package mytables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.util.Date;

/**
 * @author yangankang
 */
@Table
public enum TableTestOrder {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = long.class, nullable = false, comment = "购买用户的ID")
    userId,
    @Column(length = 16, nullable = false, comment = "唯一订单号,如：2017111013123141")
    number,
    @Column(type = long.class, comment = "收货地址ID，如果是虚拟商品则不需要")
    addressId,
    @Column(type = long.class, comment = "配送信息ID,对应商品里的运费模板，如果是虚拟商品则没有")
    deliveryId,
    @Column(type = long.class, comment = "支付方式ID")
    payMethodId,
    @Column(type = long.class, comment = "支付方式ID")
    payChannelId,
    @Column(type = long.class, comment = "发票信息")
    invoiceId,
    @Column(type = double.class, nullable = false, comment = "原价，不包含优惠的商品价格")
    costPrice,
    @Column(type = double.class, nullable = false, comment = "商品价格总计,去除优惠后的应付金额")
    totalGoodsPrice,
    @Column(type = double.class, comment = "优惠扣除价格")
    discountPrice,
    @Column(type = double.class, nullable = false, comment = "支付价格总计，必须包含totalGoodsPrice，额外的比如运费等")
    payPrice,
    @Column(type = double.class, defaultValue = "0", comment = "运费价格,免运费为0")
    freightPrice,
    @Column(type = byte.class, defaultValue = "0", comment = "订单状态 0创建")
    status,
    @Column(type = byte.class, defaultValue = "0", comment = "订单退款状态 0未退款")
    refundStatus,
    @Column(type = Date.class, nullable = false, comment = "创建订单时间")
    createTime,
    @Column(type = Date.class, nullable = false, comment = "支付订单时间")
    payTime,
    @Column(type = Date.class, nullable = false, comment = "订单发货时间")
    sendTime,
    @Column(type = Date.class, nullable = false, comment = "确认收货时间")
    getTime,
    @Column(type = Date.class, nullable = false, comment = "订单取消时间时间")
    cancelTime
}
