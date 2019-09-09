package mytables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.util.Date;

/**
 * @author yangankang
 */
@Table
public enum TableTestGoods {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = int.class, comment = "添加商品的管理员ID")
    adminId,
    @Column(type = int.class, comment = "添加商品的用户ID")
    userId,
    @Column(nullable = false, length = 200, comment = "商品名称")
    name,
    @Column(length = 200, comment = "商品副标题")
    subtitle,
    @Column(type = short.class, defaultValue = "0", comment = "商品状态 0:刚刚创建(未审核) 10:已审核 20:下架 30:上架 40:无效(审核无效)")
    status,
    @Column(type = byte.class, defaultValue = "10", comment = "商品类型 10:实物商品  11:虚拟商品")
    type,
    @Column(nullable = false, type = int.class, comment = "商品分类ID")
    gid,
    @Column(nullable = false, type = int.class, comment = "商品属性模板ID")
    pgid,
    @Column(nullable = false, type = int.class, comment = "运费模板ID")
    pwid,
    @Column(type = int.class, comment = "品牌ID")
    bid,
    @Column(length = 100, comment = "商品编号")
    serialNumber,
    @Column(type = double.class, defaultValue = "0", extDecimalFormat = "#.00", comment = "商品销售价格")
    price,
    @Column(type = double.class, defaultValue = "0", extDecimalFormat = "#.00", comment = "商品会员价格")
    vipPrice,
    @Column(type = double.class, defaultValue = "0", extDecimalFormat = "#.00", comment = "商品原价格")
    originalPrice,
    @Column(type = double.class, defaultValue = "0", extDecimalFormat = "#.00", comment = "商品成本价格")
    costPrice,
    @Column(type = int.class, defaultValue = "0", comment = "商品数量,如果使用规格商品数量是SKU商品的总和")
    amount,
    @Column(length = 10, comment = "商品单位")
    unit,
    @Column(type = double.class, extDecimalFormat = "#.00", comment = "商品重量")
    weight,
    @Column(type = double.class, extDecimalFormat = "#.00", comment = "商品体积")
    volume,
    @Column(type = int.class, comment = "售卖总量")
    sellTotal,
    @Column(type = byte.class, comment = "是否是SKU商品")
    hasSku,
    @Column(type = short.class, defaultValue = "0", comment = "删除标记，0:未删除 1:已删除(回收站内)")
    isDelete,
    @Column(type = Date.class, comment = "有效期时间,过了有效期后商品状态将变为无效")
    validTime,
    @Column(type = Date.class, comment = "创建时间")
    createTime,
    @Column(type = Date.class, comment = "修改时间")
    editTime,
    @Column(type = Date.class, comment = "上架或者下架时间")
    saleTime
}
