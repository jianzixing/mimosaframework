package org.mimosaframework.orm.auxiliary;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String id;
    private String message;
    private Date time;
    private Object source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
