package fun.billon.common.http;

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
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 对RestTemplate进行简单封装
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class RestTemplate {

    private static final Logger log = LoggerFactory.getLogger(RestTemplate.class);

    /**
     * 网络异常
     */
    public static final int CONN_ERROR = -1;

    /**
     * OK: Success!
     */
    public static final int OK = 200;

    /**
     * 支持的ssl协议
     */
    private static final String[] SUPPORT_SSL_PROTOCOLS = {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"};

    private HttpHeaders httpHeaders = new HttpHeaders();
    private org.springframework.web.client.RestTemplate restTemplate;

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
     * 初始化方法，使用指定的restTemplate初始化，如果restTemplate为null，则创建一个新的
     *
     * @param restTemplate org.springframework.web.client.RestTemplate
     */
    private void init(org.springframework.web.client.RestTemplate restTemplate) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", getSSLConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = restTemplate;
        if (restTemplate == null) {
            this.restTemplate = new org.springframework.web.client.RestTemplate();
        }
        this.restTemplate.setRequestFactory(clientHttpRequestFactory);
        this.restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    /**
     * 构造方法
     */
    public RestTemplate() {
        init(null);
    }

    /**
     * 构造方法,使用指定的restTemplate初始化
     *
     * @param restTemplate org.springframework.web.client.RestTemplate
     */
    public RestTemplate(org.springframework.web.client.RestTemplate restTemplate) {
        init(restTemplate);
    }

    /**
     * 添加header信息
     *
     * @param key   键
     * @param value 值
     */
    public void addHeader(String key, String value) {
        httpHeaders.add(key, value);
    }

    /**
     * 发送HttpGet请求。
     * 其中的queryParam必须使用uriVariables方式设置,放到requestEntity中的话Controller中无法接收到参数
     *
     * @param url          请求地址,格式为http://host/a/{pathVar1}?queryParam={queryParam}
     * @param uriVariables uri中的变量
     * @return HttpResponse
     */
    public HttpResponse get(String url, Map<String, String> uriVariables) {
        log.debug("HttpGet:" + url + ",uriVariables:" + uriVariables);
        HttpEntity requestEntity = new HttpEntity(httpHeaders);
        return httpRequest(url, HttpMethod.GET, requestEntity, uriVariables);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url          请求地址
     * @param uriVariables uri中的变量
     * @param params       post中的表单数据
     * @return HttpResponse 执行结果
     */
    public HttpResponse post(String url, Map<String, String> uriVariables, Map<String, String> params) {
        log.debug("HttpPost:" + url + ",uriVariables:" + uriVariables + ",params:" + params);
        //参数必须使用MultiValueMap封装,否则请求发出后Controller中无法接收到参数
        MultiValueMap<String, String> multiValueMapParam = new LinkedMultiValueMap<>();
        multiValueMapParam.setAll(params);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMapParam, httpHeaders);
        return httpRequest(url, HttpMethod.POST, requestEntity, uriVariables);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url          请求地址
     * @param uriVariables uri中的变量
     * @param json         json数据
     * @return HttpResponse 执行结果
     */
    public HttpResponse post(String url, Map<String, String> uriVariables, String json) {
        log.debug("HttpPost:" + url + ",uriVariables:" + uriVariables + ",json:" + json);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, httpHeaders);
        return httpRequest(url, HttpMethod.POST, requestEntity, uriVariables);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url          请求地址
     * @param uriVariables uri中的变量
     * @param params       参数
     * @return HttpResponse 执行结果
     */
    public HttpResponse put(String url, Map<String, String> uriVariables, Map<String, String> params) {
        log.debug("HttpPut:" + url + ",uriVariables:" + uriVariables + ",params:" + params);
        //参数必须使用MultiValueMap封装,否则请求发出后Controller中无法接收到参数
        MultiValueMap<String, String> multiValueMapParam = new LinkedMultiValueMap<>();
        multiValueMapParam.setAll(params);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMapParam, httpHeaders);
        return httpRequest(url, HttpMethod.PUT, requestEntity, uriVariables);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url          请求地址
     * @param uriVariables uri中的变量
     * @param json         json数据
     * @return HttpResponse 执行结果
     */
    public HttpResponse put(String url, Map<String, String> uriVariables, String json) {
        log.debug("HttpPut:" + url + ",uriVariables:" + uriVariables + ",json:" + json);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, httpHeaders);
        return httpRequest(url, HttpMethod.PUT, requestEntity, uriVariables);
    }

    /**
     * 发送HttpDelete请求。
     * 其中的queryParam必须使用uriVariables方式设置,放到requestEntity中的话Controller中无法接收到参数
     *
     * @param url          请求地址,格式为http://host/a/{pathVar1}?queryParam={queryParam}
     * @param uriVariables uri中的变量
     * @return HttpResponse 执行结果
     */
    public HttpResponse delete(String url, Map<String, String> uriVariables) {
        log.debug("HttpDelete:" + url + ",uriVariables:" + uriVariables);
        HttpEntity requestEntity = new HttpEntity(httpHeaders);
        return httpRequest(url, HttpMethod.DELETE, requestEntity, uriVariables);
    }

    /**
     * Http请求处理
     *
     * @param url           请求地址
     * @param method        请求类型
     * @param requestEntity 请求消息体
     * @param uriVariables  uri中的变量
     * @return HttpResponse
     */
    private HttpResponse httpRequest(String url, HttpMethod method, HttpEntity requestEntity, Map<String, String> uriVariables) {
        HttpResponse response = new HttpResponse();
        try {
            ResponseEntity<String> responseEntity;
            if (uriVariables != null && uriVariables.size() > 0) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class, uriVariables);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
            }
            log.debug("ResponseEntity " + responseEntity.toString() + "\n");

            if (responseEntity.getStatusCodeValue() == OK) {
                response.setResultCode(HttpResponse.RESULT_CODE_SUCCESS);
                response.setContent(responseEntity.getBody());
            } else {
                response.setErrorMsg(responseEntity.getStatusCode().getReasonPhrase());
            }
        } catch (Exception e) {
            response.setResultCode(CONN_ERROR);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

}