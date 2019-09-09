package org.mimosaframework.core.utils;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.InputStream;

/**
 * HttpClient自带的Body在某些情况下必须要有ContentLength
 * 所以自定义了一个
 */
public class InputStreamSizeBody extends InputStreamBody {
    private long size = -1L;

    public InputStreamSizeBody(InputStream in, String mimeType, String filename) {
        super(in, mimeType, filename);
    }

    public InputStreamSizeBody(InputStream in, String filename) {
        super(in, filename);
    }

    public InputStreamSizeBody(InputStream in, ContentType contentType, String filename) {
        super(in, contentType, filename);
    }

    public InputStreamSizeBody(InputStream in, ContentType contentType) {
        super(in, contentType);
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getContentLength() {
        return this.size;
    }
}
