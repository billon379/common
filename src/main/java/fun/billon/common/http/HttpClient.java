package fun.billon.common.http;

import fun.billon.common.http.api.MultipartItem;
import fun.billon.common.util.FileUtil;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对HttpClient进行简单封装
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpClient {

    /**
     * 网络异常
     */
    public static final int CONN_ERROR = -1;

    /**
     * OK: Success!
     */
    public static final int OK = 200;

    /**
     * Not Modified: There was no new data to return.
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * Bad Request: The request was invalid. An accompanying error message will explain why.
     * This is the status code will be returned during rate limiting.
     */
    public static final int BAD_REQUEST = 400;

    /**
     * Not Authorized: Authentication credentials were missing or incorrect.
     */
    public static final int NOT_AUTHORIZED = 401;

    /**
     * Forbidden: The request is understood, but it has been refused.
     * An accompanying error message will explain why.
     */
    public static final int FORBIDDEN = 403;

    /**
     * Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
     */
    public static final int NOT_FOUND = 404;

    /**
     * Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
     */
    public static final int NOT_ACCEPTABLE = 406;

    /**
     * Internal Server Error: Something is broken. Please post to the group so
     * the developer team can investigate.
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Bad Gateway: server is down or being upgraded.
     */
    public static final int BAD_GATEWAY = 502;

    /**
     * Service Unavailable: The servers are up, but overloaded with requests.Try again later.
     * The search and trend methods use this to indicate when you are being rate limited.
     */
    public static final int SERVICE_UNAVAILABLE = 503;

    /**
     * 支持的ssl协议
     */
    private static final String[] SUPPORT_SSL_PROTOCOLS = {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"};

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private List<Header> headers = new ArrayList();
    private CloseableHttpClient httpClient;

    /**
     * 获取SSLConnectionSocketFactory，用来支持ssl访问
     *
     * @return SSLConnectionSocketFactory
     */
    private SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            // 全部信任不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            return new SSLConnectionSocketFactory(builder.build(), SUPPORT_SSL_PROTOCOLS, null, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造方法
     */
    public HttpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", getSSLConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * 添加header信息
     *
     * @param key   键
     * @param value 值
     */
    public void addHeader(String key, String value) {
        headers.add(new BasicHeader(key, value));
    }

    /**
     * 发送HttpGet请求
     *
     * @param url 请求地址
     * @return HttpResponse
     */
    public HttpResponse get(String url) {
        log.debug("HttpGet:" + url);
        HttpResponse response = new HttpResponse();
        try {
            HttpGet httpGet = new HttpGet(url);
            response = httpRequest(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送HttpPost请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return HttpResponse 执行结果
     */
    public HttpResponse post(String url, Map<String, String> params) {
        log.debug("HttpPost" + url);
        List<NameValuePair> list = new ArrayList<>();
        for (String param : params.keySet()) {
            list.add(new BasicNameValuePair(param, params.get(param)));
        }
        HttpResponse response = new HttpResponse();
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(urlEncodedFormEntity);
            response = httpRequest(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送HttpPost请求
     *
     * @param url  请求地址
     * @param json json数据
     * @return HttpResponse 执行结果
     */
    public HttpResponse post(String url, String json) {
        log.debug("HttpPost" + url);
        HttpResponse response = new HttpResponse();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(stringEntity);
            response = httpRequest(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 使用multipart进行文件上传
     *
     * @param url            请求地址
     * @param multipartItems 参数列表
     * @return HttpResponse 执行结果
     */
    public HttpResponse postMultipart(String url, Map<String, MultipartItem> multipartItems) {
        log.debug("HttpPost" + url);
        HttpResponse response = new HttpResponse();
        try {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for (String param : multipartItems.keySet()) {
                MultipartItem multipartItem = multipartItems.get(param);
                if (multipartItem.isFormField()) {
                    builder.addTextBody(param, multipartItem.getValue());
                } else {
                    builder.addBinaryBody(param, multipartItem.getInputStream(), ContentType.DEFAULT_BINARY, multipartItem.getFileName());
                }
            }
            httpPost.setEntity(builder.build());
            response = httpRequest(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送HttpPut请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return HttpResponse 执行结果
     */
    public HttpResponse put(String url, Map<String, String> params) {
        log.debug("HttpPut" + url);
        List<NameValuePair> list = new ArrayList<>();
        for (String param : params.keySet()) {
            list.add(new BasicNameValuePair(param, params.get(param)));
        }
        HttpResponse response = new HttpResponse();
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            HttpPut httpPut = new HttpPut(url);
            httpPut.setEntity(urlEncodedFormEntity);
            response = httpRequest(httpPut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送HttpPut请求
     *
     * @param url  请求地址
     * @param json json数据
     * @return HttpResponse 执行结果
     */
    public HttpResponse put(String url, String json) {
        log.debug("HttpPut" + url);
        HttpResponse response = new HttpResponse();
        try {
            HttpPut httpPut = new HttpPut(url);
            httpPut.addHeader("Content-Type", "application/json;charset=utf-8");
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            httpPut.setEntity(stringEntity);
            response = httpRequest(httpPut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送HttpDelete请求
     *
     * @param url 请求地址
     * @return HttpResponse 执行结果
     */
    public HttpResponse delete(String url) {
        log.debug("HttpDelete" + url);
        HttpResponse response = new HttpResponse();
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            response = httpRequest(httpDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Http请求处理
     *
     * @param request HttpGet,HttpPost
     * @return HttpResponse
     */
    private HttpResponse httpRequest(HttpUriRequest request) {
        HttpResponse response = new HttpResponse();
        CloseableHttpResponse httpResponse = null;
        try {
            for (Header header : headers) {
                request.setHeader(header);
            }
            httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            response.setResultCode(statusCode);

            String content = FileUtil.inputStream2String(httpResponse.getEntity().getContent());
            log.debug("HttpResponse " + content + "\n");

            if (statusCode == OK) {
                response.setResultCode(HttpResponse.RESULT_CODE_SUCCESS);
                response.setContent(content);
            } else {
                response.setErrorMsg(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            response.setResultCode(CONN_ERROR);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

}