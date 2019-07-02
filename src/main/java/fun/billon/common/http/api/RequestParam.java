package fun.billon.common.http.api;

import java.util.Map;

/**
 * 请求参数
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RequestParam {

    /**
     * 是否是multipart请求
     *
     * @return 是否是multipart请求
     */
    boolean isMultipartRequest();

    /**
     * 生成表单请求参数
     *
     * @return 表单请求参数
     */
    String genFormParam();

    /**
     * 生成multipart请求参数
     *
     * @return multipart请求参数
     */
    Map<String, MultipartItem> genMultipartParm();

}
