package fun.billon.common.http;

import fun.billon.common.http.api.MultipartItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试用例
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpClientTest {

    /**
     * 测试httpclient
     */
    @Test
    public void get() {
        HttpClient client = new HttpClient();
        HttpResponse response = client.get("http://www.baidu.com");
        Assert.assertTrue("httpclient test success", response.getResultCode() == HttpResponse.RESULT_CODE_SUCCESS);
    }

    @Test
    public void testPostMultipart() {
        HttpClient client = new HttpClient();
        Map<String, MultipartItem> multipartItems = new HashMap<>();
        try {
            multipartItems.put("form-param", new MultipartItem("form-value"));
            multipartItems.put("bin-file1", new MultipartItem("driving_license.jpg", new FileInputStream("d:/driving_license.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.postMultipart("http://localhost:8080/api/MediaUpLoad", multipartItems);
    }
}