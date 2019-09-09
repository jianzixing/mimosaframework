package mytables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.util.Date;

/**
 * @author yangankang
 */
@Table
public enum TableTestUser {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(extMinLength = 6, length = 30, nullable = false)
    userName,
    @Column(length = 32, nullable = false)
    password,
    @Column(length = 256, extRegExp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
    email,
    @Column(length = 30, unique = true, comment = "微信登录用户id")
    openid,
    @Column(length = 30, comment = "用户唯一标识,只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段")
    unionid,
    @Column(length = 60, comment = "用户accessToken")
    accessToken,
    @Column(type = int.class, comment = "access_token接口调用凭证超时时间，单位（秒)")
    expiresIn,
    @Column(length = 60, comment = "用户刷新access_token")
    refreshToken,
    @Column(length = 14, extRegExp = "^1[345789]\\d{9}$")
    phone,
    @Column(type = short.class, defaultValue = "0")
    validEmail,
    @Column(type = short.class, defaultValue = "0")
    validPhone,
    @Column(type = short.class, defaultValue = "1")
    enable,
    @Column(length = 50, comment = "昵称")
    nick,
    @Column(type = short.class, defaultValue = "0", comment = "年龄")
    age,
    @Column(type = byte.class, defaultValue = "0", comment = "性别 0未知  1男 2女")
    gender,
    @Column(length = 500, comment = "头像")
    avatar,
    @Column(length = 16, nullable = false, comment = "密码盐值")
    signature,
    @Column(type = Date.class, comment = "生日")
    birthday,
    @Column(type = short.class, defaultValue = "0", comment = "是否结婚 0:结婚 1:没结婚")
    isMarried,
    @Column(length = 300, comment = "用户地址")
    address,
    @Column(type = Date.class, extCanUpdate = false)
    registerTime
}
