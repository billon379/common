package fun.billon.common.cache.annotation;

import fun.billon.common.cache.CacheType;
import fun.billon.common.cache.interceptor.CacheInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用来标识要缓存的方法
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * 命名空间
     */
    String namespace();

    /**
     * 键值
     */
    String key();

    /**
     * 缓存数据类型
     */
    Class type();

    /**
     * 要使用的缓存结构
     */
    CacheType cacheType();

    /**
     * 设置缓存过期时间(默认2小时过期)
     */
    long expire() default 60 * 60 * 2;

    /**
     * 缓存处理拦截器，可以对缓存结果做处理
     */
    Class<? extends CacheInterceptor> cacheInterceptor() default CacheInterceptor.class;

}