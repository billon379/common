package fun.billon.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用来标识清除缓存的方法
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvict {
    /**
     * 命名空间
     */
    String namespace();

    /**
     * 键值
     */
    String key();
}
