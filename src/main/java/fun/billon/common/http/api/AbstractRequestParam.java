package fun.billon.common.http.api;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;

import java.util.Map;

/**
 * 请求参数适配器
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public abstract class AbstractRequestParam implements RequestParam {

    /**
     * 是否是multipart请求
     */
    @XStreamOmitField
    protected boolean isMultipartRequest;

    /**
     * 生成表单请求参数
     *
     * @return 表单请求参数
     */
    @Override
    public String genFormParam() {
        return null;
    }

    /**
     * 生成multipart请求参数
     *
     * @return multipart请求参数
     */
    @Override
    public Map<String, MultipartItem> genMultipartParm() {
        return null;
    }

}