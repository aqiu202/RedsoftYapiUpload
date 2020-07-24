package com.redsoft.idea.plugin.yapiv2.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http client 请求工具类
 *
 * @date 2018/1/26.
 */
public final class HttpClientUtils {

    private static final int socketTimeout = 10000;
    private static final int connectionTimeout = 10000;
    private static final int connectionRequestTimeout = 10000;

    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectionTimeout)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .build();

    private static volatile CloseableHttpClient httpclient;
    private static CloseableHttpClient tlsClient;//TLSv1.2协议对应client

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    private HttpClientUtils() {

    }

    /**
     * 初始化连接池
     */
    private static void init() throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (x509Certificates, s) -> true);
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry
                = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                PlainConnectionSocketFactory.INSTANCE).register("https",
                sslsf).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        int maxTotal = 100;
        cm.setMaxTotal(maxTotal);
        int defaultMaxPerRoute = 100;
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        httpclient = HttpClients.custom()
                .setConnectionManager(cm).setDefaultRequestConfig(requestConfig)
                .build();

        //支持TLSv1.2协议
        Registry<ConnectionSocketFactory> tlsRegistry
                = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                PlainConnectionSocketFactory.INSTANCE).register("https",
                new SSLConnectionSocketFactory(createIgnoreVerifySSL())).build();
        PoolingHttpClientConnectionManager tlsCM = new PoolingHttpClientConnectionManager(
                tlsRegistry);
        tlsCM.setMaxTotal(maxTotal);
        tlsCM.setDefaultMaxPerRoute(defaultMaxPerRoute);
        tlsClient = HttpClients.custom()
                .setConnectionManager(tlsCM).setDefaultRequestConfig(requestConfig)
                .build();
    }

    @PreDestroy
    public void destroy() {
        try {
            httpclient.close();
            tlsClient.close();
        } catch (Exception e) {
            logger.error("http client 池销毁异常:", e);
        }
    }

    public static CloseableHttpClient getHttpclient() {
        if (httpclient == null) {
            synchronized (HttpClientUtils.class) {
                if (httpclient == null) {
                    try {
                        init();
                    } catch (Exception e) {
                        logger.error("初始化 http client异常", e);
                    }
                }
            }
        }
        return httpclient;
    }

    /**
     * 获取HttpGet
     *
     * @return HttpGet
     */
    public static HttpGet getHttpGet(String url, String accept, String contentType) {
        HttpGet httpGet;
        httpGet = new HttpGet(url);
        if (accept != null) {
            httpGet.setHeader("Accept", accept);
        }

        if (contentType != null) {
            httpGet.setHeader("Content-Type", contentType);
        }
        return httpGet;
    }

    /**
     * CloseableHttpResponse 转字符串
     *
     * @return String
     */
    public static String ObjectToString(CloseableHttpResponse response, String charset)
            throws IOException {
        try {
            HttpEntity resEntity = response.getEntity();
            String responseBaby = null;
            if (resEntity != null) {
                responseBaby = EntityUtils.toString(resEntity, charset);
            }
            return responseBaby;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private static SSLContext createIgnoreVerifySSL()
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }


}
