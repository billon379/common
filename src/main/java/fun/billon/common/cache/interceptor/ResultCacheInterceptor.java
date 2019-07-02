package fun.billon.common.cache.interceptor;

import fun.billon.common.model.ResultModel;

/**
 * ResultModel的缓存处理拦截器。
 * H表示缓存命中时要返回的数据类型为ResultModel,
 * M表示缓存未命中时要返回的数据类型为ResultModel.data
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class ResultCacheInterceptor<H, M> implements CacheInterceptor<H, M> {

    /**
     * 缓存命中后做的处理
     *
     * @param result 需要处理的结果
     * @return 处理后的结果
     */
    @Override
    public H onCacheHit(Object result) {
        ResultModel resultModel = new ResultModel();
        resultModel.setData(result);
        return (H) resultModel;
    }

    /**
     * 缓存未命中后做的处理
     *
     * @param result 需要处理的结果
     * @return 处理后的结果
     */
    @Override
    public M onCacheMiss(Object result) {
        ResultModel resultModel = (ResultModel) result;
        if (resultModel.getCode() == ResultModel.RESULT_SUCCESS) {
            return (M) resultModel.getData();
        }
        return null;
    }

}