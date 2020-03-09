package org.mimosaframework.springmvc.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.springmvc.exception.StockCode;

import java.util.List;

public class ResponsePageMessage extends ResponseMessage {
    private long total;
    private List<ModelObject> records;
    private ModelObject page;

    public ResponsePageMessage(Object data) {
        super(data);
    }

    public ResponsePageMessage(StockCode code, String msg) {
        super(code, msg);
    }

    public ResponsePageMessage(String code, String msg) {
        super(code, msg);
    }

    public ResponsePageMessage(List<ModelObject> records) {
        this.records = records;
        this.setCode(100);
    }

    public ResponsePageMessage(ResponseMessage responseMessage) {
        if (responseMessage.getCode() instanceof Integer)
            this.setCode((Integer) responseMessage.getCode());
        if (responseMessage.getCode() instanceof String)
            this.setCode((String) responseMessage.getCode());
        this.setMsg(responseMessage.getMsg());
        this.setData(responseMessage.getData());
    }

    public ResponsePageMessage(Paging paging) {
        if (paging != null) {
            this.total = paging.getCount();
            this.records = paging.getObjects();
        }
        this.setCode(100);
    }

    public ResponsePageMessage(long total, List<ModelObject> records) {
        this.total = total;
        this.records = records;
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

    public List<ModelObject> getRecords() {
        return records;
    }

    public void setRecords(List<ModelObject> records) {
        this.records = records;
    }

    public ModelObject getPage() {
        return page;
    }

    public void setPage(ModelObject page) {
        this.page = page;
    }
}
