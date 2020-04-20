package org.mimosaframework.orm;

/**
 * 通过SessionTemplate可以获取各种web服务的代理实现
 */
public interface SessionTemplate extends Session, TransactionTemplate, Template, AuxiliaryTemplate {

}
