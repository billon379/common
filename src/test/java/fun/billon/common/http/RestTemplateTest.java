package fun.billon.common.http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by billon on 2018/1/30.
 */
public class RestTemplateTest {

    private RestTemplate restTemplate;

    @Before
    public void init() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void testGet() {
        HttpResponse response = restTemplate.get("http://www.baidu.com", null);
        Assert.assertTrue("httpclient test success", response.getResultCode() == HttpResponse.RESULT_CODE_SUCCESS);
    }
}