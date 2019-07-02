package fun.billon.common.cache.interceptor;

/**
 * 缓存拦截器,配合RedisAspect使用,对缓存结果做处理。
 * H表示缓存命中时要返回的数据类型,M表示缓存未命中时要返回的数据类型
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CacheInterceptor<H, M> {

    /**
     * 缓存命中后做的处理
     *
     * @param result 需要处理的结果
     * @return 处理后的结果
     */
    H onCacheHit(Object result);

    /**
     * 缓存未命中后做的处理
     *
     * @param result 需要处理的结果
     * @return 处理后的结果
     */
    M onCacheMiss(Object result);

}