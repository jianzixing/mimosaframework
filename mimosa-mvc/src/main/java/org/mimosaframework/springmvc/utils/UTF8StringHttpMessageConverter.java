package org.mimosaframework.springmvc.utils;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Created by qmt on 2016/1/16.
 * Desc
 */
public class UTF8StringHttpMessageConverter extends StringHttpMessageConverter {

    private static final MediaType utf8 = new MediaType("text", "plain", Charset.forName("UTF-8"));
    private boolean writeAcceptCharset = true;

    @Override
    protected MediaType getDefaultContentType(String dumy) {
        return utf8;
    }

    protected List<Charset> getAcceptedCharsets() {
        return List.of(StandardCharsets.UTF_8);
    }

    protected void writeInternal(String s, HttpOutputMessage outputMessage)
            throws IOException {
        if (this.writeAcceptCharset) {
            outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
        }
        Charset charset = StandardCharsets.UTF_8;
        FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(),
                charset));
    }

    public boolean isWriteAcceptCharset() {
        return writeAcceptCharset;
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.writeAcceptCharset = writeAcceptCharset;
    }

}

