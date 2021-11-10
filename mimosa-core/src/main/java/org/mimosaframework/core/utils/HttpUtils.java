package org.mimosaframework.core.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUtils {

    public static String get(String url, String charset) throws IOException {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpCilent.execute(httpGet);
            String json = EntityUtils.toString(response.getEntity(), charset);
            return json;
        } finally {
            try {
                httpCilent.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String get(String url) throws IOException {
        return get(url, "UTF-8");
    }

    public static String get(String url, Map<String, Object> params, String charset) throws IOException {
        String p = mapToParams(params);
        if (StringTools.isNotEmpty(p)) {
            return get(url + "?" + p, charset);
        } else {
            return get(url, charset);
        }
    }

    public static String get(String url, Map<String, Object> params) throws IOException {
        return get(url, params, "UTF-8");
    }

    public static Map<String, String> paramsToMap(String str) {
        if (StringTools.isNotEmpty(str)) {
            Map<String, String> map = new LinkedHashMap<>();
            String[] s1 = str.split("&");
            for (String s2 : s1) {
                String[] s3 = s2.split("=");
                if (s3.length > 0) map.put(s3[0], s3.length > 1 ? s3[1] : null);
            }
            return map;
        }
        return null;
    }

    public static String mapToParams(Map<String, Object> params) {
        return mapToParams(params, true);
    }

    public static String mapToParams(Map<String, Object> params, boolean encode) {
        if (params != null) {
            StringBuilder sb = new StringBuilder();
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                sb.append(String.valueOf(entry.getKey()));
                sb.append("=");
                String value = String.valueOf(entry.getValue());
                if (encode) {
                    try {
                        sb.append(URLEncoder.encode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    sb.append(value);
                }
                if (iterator.hasNext()) {
                    sb.append("&");
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static String post(String url, String contentType, String charset, String data) throws IOException {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            //设置请求的报文头部的编码
            httpPost.setHeader(new BasicHeader("Content-Type", contentType + "; charset=" + charset));
            //设置期望服务端返回的编码
            httpPost.setHeader(new BasicHeader("Accept", "text/plain;charset=" + charset));
            if (StringTools.isNotEmpty(data)) {
                StringEntity s = new StringEntity(data, charset);
                httpPost.setEntity(s);
            }
            CloseableHttpResponse response = httpCilent.execute(httpPost);
            String json = EntityUtils.toString(response.getEntity(), charset);
            return json;
        } finally {
            try {
                httpCilent.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String postJson(String url, String data) throws IOException {
        return post(url, "application/json", "utf-8", data);
    }

    public static String postJson(String url, Map<String, Object> params) throws IOException {
        String p = mapToParams(params);
        if (StringTools.isNotEmpty(p)) {
            return postJson(url, p);
        } else {
            return postJson(url, "");
        }
    }

    public static String post(String url, String data) throws IOException {
        return post(url, "text/html", "utf-8", data);
    }

    public static String post(String url, Map<String, Object> params) throws IOException {
        String p = mapToParams(params);
        if (StringTools.isNotEmpty(p)) {
            return post(url, p);
        } else {
            return post(url, "");
        }
    }

    public static String upload(String url, Map<String, Object> params) throws IOException {
        if (params != null && params.size() > 0) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost(url);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof InputStream) {
                        String fileName = (String) params.get(key + "_file_name");
                        Long fileSize = (Long) params.get(key + "_file_size");
                        if (StringTools.isEmpty(fileName)) {
                            throw new IllegalArgumentException("must set file name in map by key:" + key + "_file_name");
                        }
                        if (fileSize == null) {
                            builder.addBinaryBody(key, (InputStream) value, ContentType.MULTIPART_FORM_DATA, fileName); //相当于<input type="file" name="media"/>
                        } else {
                            InputStreamSizeBody body = new InputStreamSizeBody((InputStream) value, fileName);
                            body.setSize(fileSize);
                            builder.addPart(key, body);
                        }
                    }
                    if (value instanceof String) {
                        StringBody comment = new StringBody((String) value, ContentType.MULTIPART_FORM_DATA);
                        builder.addPart(key, comment); //相当于<input type="text" name="name" value=name>
                    }
                    if (value instanceof File) {
                        File file = (File) value;
                        builder.addBinaryBody(key, file);
                    }
                    if (value instanceof Integer || value instanceof Long || value instanceof Double
                            || value instanceof Short || value instanceof Byte) {
                        StringBody comment = new StringBody(String.valueOf(value), ContentType.MULTIPART_FORM_DATA);
                        builder.addPart(key, comment);
                    }
                }

                HttpEntity httpEntity = builder.build();
                httpPost.setEntity(httpEntity);
                CloseableHttpResponse response = httpClient.execute(httpPost);
                try {
                    HttpEntity resEntity = response.getEntity();
                    String responseText = null;
                    if (resEntity != null) {
                        responseText = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    }
                    //销毁
                    EntityUtils.consume(resEntity);
                    return responseText;
                } finally {
                    response.close();
                }
            } finally {
                if (httpClient != null) httpClient.close();
            }
        }
        return null;
    }

    public static void download(String url, OutputStream outputStream) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream in = null;
        try {
            HttpGet httpget = new HttpGet(url);
            response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
            int l;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                outputStream.write(tmp, 0, l);
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真
            }
            outputStream.flush();
            EntityUtils.consume(entity);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String httpSSLConnector(String url, String content, String charset, String keyStoreType, InputStream keyStoreFile, String keyStorePassword, boolean isPost) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        CloseableHttpClient httpclient = null;
        if (StringTools.isEmpty(keyStoreType)) {
            keyStoreType = "PKCS12";
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            try {
                keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
            } finally {
                keyStoreFile.close();
            }

            // Trust own CA and all self-signed certs
            SSLContext sslcontext = org.apache.http.conn.ssl.SSLContexts.custom()
                    .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            CloseableHttpResponse response = null;
            if (isPost) {
                HttpPost httpPost = new HttpPost(url);
                //设置期望服务端返回的编码
                httpPost.setHeader(new BasicHeader("Accept", "text/plain;charset=" + charset));
                httpPost.setEntity(new StringEntity(content, charset));
                response = httpclient.execute(httpPost);
            } else {
                HttpGet httpGet = new HttpGet(url);
                response = httpclient.execute(httpGet);
            }
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseText = EntityUtils.toString(entity, charset);
                    return responseText;
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String postSSL(String url, String content, String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return httpSSLConnector(url, content, "UTF-8", keyStoreType, keyStoreFile, keyStorePassword, true);
    }

    public static String getSSL(String url, String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return httpSSLConnector(url, null, "UTF-8", keyStoreType, keyStoreFile, keyStorePassword, true);
    }

    public static String postP12SSL(String url, String content, InputStream keyStoreFile, String keyStorePassword) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return httpSSLConnector(url, content, "UTF-8", "PKCS12", keyStoreFile, keyStorePassword, true);
    }

    public static String getP12SSL(String url, String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return httpSSLConnector(url, null, "UTF-8", "PKCS12", keyStoreFile, keyStorePassword, true);
    }
}
