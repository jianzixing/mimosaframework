package org.mimosaframework.orm;

import org.mimosaframework.orm.builder.XmlConfigBuilder;
import org.mimosaframework.orm.exception.ContextException;

import java.io.InputStream;

public class XmlAppContext extends BeanAppContext {
    public XmlAppContext(String path) throws ContextException {
        super();
        XmlConfigBuilder configBuilder = new XmlConfigBuilder(path);
        this.setBeanAppContext(configBuilder);
    }

    public XmlAppContext(InputStream inputStream) throws ContextException {
        super();
        XmlConfigBuilder configBuilder = new XmlConfigBuilder(inputStream);
        this.setBeanAppContext(configBuilder);
    }
}
