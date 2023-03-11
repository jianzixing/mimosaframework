package org.mimosaframework.springmvc.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.serializer.SerializeConfig;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.springmvc.DyToStringSerializer;
import org.mimosaframework.springmvc.exception.StockCode;

import java.util.List;

public class ResponsePageMessage<T> extends ResponseMessage<T> {
    protected long total;
    protected ModelObject page;

    public ResponsePageMessage(StockCode code, String msg) {
        super(code, msg);
    }

    public ResponsePageMessage(String code, String msg) {
        super(code, msg);
    }

    public ResponsePageMessage(int code, String msg) {
        super(code, msg);
    }

    public ResponsePageMessage(Object data) {
        super(data);
    }

    public ResponsePageMessage(ResponseMessage responseMessage) {
        if (responseMessage.getCode() instanceof Integer)
            this.setCode((Integer) responseMessage.getCode());
        if (responseMessage.getCode() instanceof String)
            this.setCode((String) responseMessage.getCode());
        this.setMsg(responseMessage.getMsg());
        this.setData((T) responseMessage.getData());
    }

    public ResponsePageMessage(Paging paging) {
        if (paging != null) {
            this.total = paging.getCount();
            this.data = (T) paging.getObjects();
        }
        this.setCode(100);
    }

    public ResponsePageMessage(long total, T data) {
        this.total = total;
        this.data = data;
        this.setCode(100);
    }

    public ResponsePageMessage() {
        this.total = 0;
        this.setCode(100);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ModelObject getPage() {
        return page;
    }

    public void setPage(ModelObject page) {
        this.page = page;
    }

    @Override
    public String toString() {
        // 将长long类型转换成字符串
        SerializeConfig config = new SerializeConfig();
        config.put(Long.class, DyToStringSerializer.instance);
        config.put(Long.TYPE, DyToStringSerializer.instance);
        return ModelObject.toJSONString(this, config);
    }

    public Paging toPaging() {
        return new Paging(total, (List) data);
    }
}
