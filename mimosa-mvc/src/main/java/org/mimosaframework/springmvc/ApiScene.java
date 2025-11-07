package org.mimosaframework.springmvc;

public enum ApiScene {
    /**
     * 默认
     */
    DEFAULT,
    /**
     * 官方网站请求
     */
    OFFICIALLY,
    /**
     * 后台管理接口
     */
    ADMIN,
    /**
     * C/B端用户管理平台接口
     */
    MANAGER,
    /**
     * 触发事件接口
     */
    EVENT,
    /**
     * 开放式需鉴权API接口
     */
    API,
    /**
     * 开放式无鉴权API接口
     */
    FRONT,
    /**
     * 非开放式鉴权API接口
     */
    CALL
}
