package fun.billon.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分页基类Model
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class PaginationModel {

    /**
     * 分页页码
     */
    @JSONField(serialize = false)
    private int pageIndex;

    /**
     * 每页数据条数
     */
    @JSONField(serialize = false)
    private int pageSize;

    /**
     * 搜索的关键字
     */
    @JSONField(serialize = false)
    private String keywords;

    /**
     * 分页条件:时间
     */
    @JSONField(serialize = false)
    private Date qt;

    /**
     * 分页条件:起始时间
     */
    @JSONField(serialize = false)
    private Date qtStart;

    /**
     * 分页条件:结束时间
     */
    @JSONField(serialize = false)
    private Date qtEnd;

    /**
     * 排序条件
     */
    @JSONField(serialize = false)
    private String orderBy;

    /**
     * 过滤条件:非空字段(list(i) IS NOT NULL)
     */
    @JSONField(serialize = false)
    private List<String> filterNotNull;

    /**
     * 过滤条件:字段不等于(map(ki) NOT IN(#{map(ki).vj},#{map(ki).v(j+1)}))
     */
    @JSONField(serialize = false)
    private Map<String, List<String>> filterNotEqual;

    /**
     * 过滤条件:模糊匹配(list(i) LIKE %keywords%)
     */
    @JSONField(serialize = false)
    private List<String> filterLike;

    public PaginationModel() {
        this.pageIndex = 1;
        this.pageSize = 20;
    }

}