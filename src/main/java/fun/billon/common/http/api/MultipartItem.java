package fun.billon.common.http.api;

import lombok.Data;

import java.io.InputStream;

/**
 * Multipart文件上传的参数条目
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class MultipartItem {

    /**
     * 属性值
     */
    private String value;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 字节流
     */
    private InputStream inputStream;

    /**
     * 是否是表单属性
     */
    private boolean isFormField;

    /**
     * 构造方法，设置form表单信息
     *
     * @param value 属性值
     */
    public MultipartItem(String value) {
        this.value = value;
        this.isFormField = true;
    }

    /**
     * 构造方法，设置二进制数据
     *
     * @param fileName    文件名
     * @param inputStream 字节流
     */
    public MultipartItem(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.isFormField = false;
    }

    @Override
    public String toString() {
        return "MultipartItem{" +
                ", value='" + value + '\'' +
                ", fileName='" + fileName + '\'' +
                ", inputStream=" + inputStream +
                ", isFormField=" + isFormField +
                '}';
    }

}