package fun.billon.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HttpResponse工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpResponseUtils {

    /**
     * CONTENT_TYPE_TEXT_HTML
     */
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=utf-8";

    /**
     * 将数据写入HttpServletResponse
     *
     * @param response HttpServletResponse
     * @param data     要写入的数据
     * @throws IOException
     */
    public static void write(HttpServletResponse response, String data) throws IOException {
        response.setContentType(CONTENT_TYPE_TEXT_HTML);
        response.getWriter().write(data);
    }

}