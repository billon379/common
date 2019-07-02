package fun.billon.common.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fun.billon.common.cache.annotation.CacheEvict;
import fun.billon.common.cache.annotation.Cacheable;
import fun.billon.common.cache.interceptor.CacheInterceptor;
import fun.billon.common.util.DateConverter;
import fun.billon.common.util.StringUtils;
import lombok.Data;
import org.apache.commons.beanutils.ConvertUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Redis缓存实现。
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class RedisAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisAspect.class);

    /**
     * 缓存键的格式
     */
    public static final String PATTERN_CACHE_KEY = "([#$]\\{[^\\}]*\\})*";

    /**
     * 缓存键分组格式
     */
    public static final Pattern PATTERN_CACHE_KEY_GROUP = Pattern.compile("[#$]\\{[^\\}]*\\}");

    /**
     * redis模板
     */
    private RedisTemplate redisTemplate;

    /**
     * 获取参数索引
     *
     * @param paramNames 参数列表
     * @param param      参数名
     * @return 参数索引值
     */
    private int getParamIndex(String[] paramNames, String param) {
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(param)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取对象属性
     *
     * @param obj   对象
     * @param field 属性
     * @return 对象的属性值
     */
    private Object getField(Object obj, String field) throws Exception {
        String[] splitFiled = field.split("\\.");
        String getter = "get" + splitFiled[0].substring(0, 1).toUpperCase() + splitFiled[0].substring(1);
        Method method = obj.getClass().getMethod(getter);
        Object result = method.invoke(obj);
        if (splitFiled.length > 1) {
            return getField(result, field.substring(field.indexOf(".") + 1));
        }
        return result;
    }

    /**
     * 根据namespace,field,fieldValue生成redis键。
     * 1）当field格式为#{model.field}或#{field}时,需要从属性中获取键值,生成的redis键格式为：namespace:field:fieldValue
     * 2) 当field格式为${model.field}或${field}时,需要从属性中获取键值,生成的redis键格式为：namespace:fieldValue
     *
     * @param namespace  命名空间
     * @param annoKey    缓存键注解
     * @param paramNames aop拦截到方法的参数名称
     * @param args       aop拦截到方法的参数
     * @return 从属性中获取到的键值, 格式为：namespace:field:fieldValue
     * @throws Exception
     */
    private String getRdbKeyDynamic(String namespace, String annoKey, String[] paramNames, Object[] args) throws Exception {
        /*
         * annoKey的格式可能包含多个属性,例：#{model.field}${model.field}
         * 1)将annoKey分组
         * 2)获取属性的值并进行拼接
         * 3)当属性不存在,或对应的值为空时,不进行拼接
         * 4)当所有属性都是空时,返回null
         */
        StringBuilder sb = new StringBuilder(namespace);
        Matcher matcher = PATTERN_CACHE_KEY_GROUP.matcher(annoKey);
        while (matcher.find()) {
            LOGGER.info("============ RedisAspect annoKey {} ==============", matcher.group());
            // 分组
            String groupField = matcher.group();
            // 将#{model.field}中的字串提取出来model.field
            String field = groupField.substring(2, groupField.length() - 1);

            /*
             * 检查参数名称
             * 1)如果annoKey格式为#{model.field},获取model对应的参数,如果参数名称不匹配,返回null
             * 2)如果annoKey格式为#{field},获取field对应的参数,如果参数名称不匹配,返回null
             */
            int index = getParamIndex(paramNames, field.split("\\.")[0]);
            if (index == -1) {
                LOGGER.info("============ RedisAspect field key {} does not exits ==============", annoKey);
                return null;
            }

            /*
             * 获取参数值
             * 1)如果annoKey格式为#{model.field},获取model中field的值,值为空,返回null
             * 2)如果annoKey格式为#{field},获取参数field的值,值为空,返回null
             */
            Object fieldValue;
            int fieldIndex = field.indexOf(".");
            if (fieldIndex > 0) {
                // annoKey格式为#{model.field},获取model中field的值
                field = field.substring(fieldIndex + 1);
                fieldValue = getField(args[index], field);
            } else {
                // annoKey格式为#{field},获取参数field的值
                fieldValue = args[index];
            }

            // 将获取到的fieldValue拼接到键
            if (fieldValue != null) {
                LOGGER.info("============ RedisAspect get field key {} = {} ==============", groupField, fieldValue);
                if (groupField.startsWith("#")) {
                    // 如果是以#开头,则将field拼接到键
                    sb.append(":").append(field.substring(field.lastIndexOf(".") + 1));
                }
                sb.append(":").append(fieldValue);
            }
        }

        // 属性值全为空，返回null
        if (sb.toString().equals(namespace)) {
            return null;
        }

        return sb.toString();
    }

    /**
     * 获取要缓存的键
     * Entry格式：namespace:field:fieldValue
     * 1）当field格式为#{model.field}或#{field}时,需要从属性中获取键值,生成的redis键格式为：namespace:field:fieldValue
     * 2) 当field格式为${model.field}或${field}时,需要从属性中获取键值,生成的redis键格式为：namespace:fieldValue
     * 3）当field格式为field(固定字串)时,生成的redis键格式为：namespace:field
     *
     * @param joinPoint 切入点
     * @param namespace 缓存命名空间
     * @param annoKey   注解上的键
     * @return 要缓存的键
     */
    private String getRdbKey(ProceedingJoinPoint joinPoint, String namespace, String annoKey) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 参数名列表
        String[] paramNames = methodSignature.getParameterNames();

        // 参数列表
        Object[] args = joinPoint.getArgs();
        if (annoKey.matches(PATTERN_CACHE_KEY)) {
            // 键值需要动态获取
            return getRdbKeyDynamic(namespace, annoKey, paramNames, args);
        } else {
            // 键值是静态字串
            LOGGER.info("============ RedisAspect get annotation key {} ==============", annoKey);
            return namespace + ":" + annoKey;
        }
    }

    /**
     * 获取要缓存的键
     * Entry格式：namespace:field:fieldValue
     *
     * @param joinPoint 切入点
     * @param cacheable 缓存的注解
     * @return 要缓存的键
     */
    private String getCacheableKey(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        return getRdbKey(joinPoint, cacheable.namespace(), cacheable.key());
    }

    /**
     * 获取要清除缓存的键
     * Entry格式：namespace:field:fieldValue
     *
     * @param joinPoint  切入点
     * @param cacheEvict 清除缓存的注解
     * @return 要缓存的键
     */
    private String getCacheEvictKey(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        return getRdbKey(joinPoint, cacheEvict.namespace(), cacheEvict.key());
    }

    /**
     * 从缓存中读取数据
     *
     * @param cacheType 缓存数据结构
     * @param type      要缓存的数据类型
     * @param rdbKey    缓存键值
     * @return 从缓存中获取到的数据
     */
    private Object getValue(CacheType cacheType, Class type, String rdbKey) {
        try {
            switch (cacheType) {
                // 字串
                case VALUE:
                    Object value = redisTemplate.boundValueOps(rdbKey).get();
                    if (value != null) {
                        return JSONObject.parseObject(value.toString(), type);
                    }
                    break;
                // hash
                case HASH:
                    // 从redis中获取数据
                    Map map = redisTemplate.boundHashOps(rdbKey).entries();
                    if (map.size() > 0) {
                        // 从redis中获取数据成功
                        return new BeanUtilsHashMapper(type).fromHash(map);
                    }
                    break;
                // 列表
                case LIST:
                    List<String> list = redisTemplate.boundListOps(rdbKey).range(0, -1);
                    List retList = new ArrayList();
                    if (list.size() > 0) {
                        for (String v : list) {
                            retList.add(JSON.parseObject(v, type));
                        }
                        return retList;
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.info("============ RedisAspect redis disconnected ==============");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将数据写入缓存
     *
     * @param cacheType 缓存数据结构
     * @param type      要缓存的数据类型
     * @param rdbKey    缓存键值
     * @param value     要缓存的数据
     * @param expire    过期时间
     */
    private void setValue(CacheType cacheType, Class type, String rdbKey, Object value, long expire) {
        try {
            switch (cacheType) {
                // 字串
                case VALUE:
                    redisTemplate.boundValueOps(rdbKey).set(JSONObject.toJSONString(value));
                    break;
                // hash
                case HASH:
                    redisTemplate.boundHashOps(rdbKey).putAll(new BeanUtilsHashMapper(type).toHash(value));
                    break;
                // 列表
                case LIST:
                    if (value instanceof List) {
                        for (Object v : (List) value) {
                            redisTemplate.boundListOps(rdbKey).rightPush(JSON.toJSONString(v));
                        }
                    } else {
                        redisTemplate.boundListOps(rdbKey).rightPush(JSON.toJSONString(value));
                    }
                    break;
                default:
                    break;
            }

            /*
             * 设置缓存过期时间
             */
            if (expire > 0) {
                redisTemplate.expire(rdbKey, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            LOGGER.info("============ RedisAspect redis disconnected ==============");
            e.printStackTrace();
        }
    }

    /**
     * 处理Cacheable注解
     *
     * @param joinPoint 切入点
     * @param cacheable Cacheable注解
     * @return 通过缓存或db获取到的数据
     */
    private Object cache(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        // 获取注解中的键值
        String rdbKey = getCacheableKey(joinPoint, cacheable);
        LOGGER.info("============ RedisAspect cache key {} ==============", rdbKey);

        /*
         * 缓存键不存在,则不需要访问redis,直接返回执行结果
         */
        if (StringUtils.isEmpty(rdbKey)) {
            return joinPoint.proceed(joinPoint.getArgs());
        }

        Object result;
        /*
         * 访问redis
         */
        result = getValue(cacheable.cacheType(), cacheable.type(), rdbKey);
        if (result != null) {
            // 缓存命中
            LOGGER.info("============ RedisAspect cache hit ==============");
            Class clz = cacheable.cacheInterceptor();
            if (!clz.isInterface()) {
                // 如果配置了缓存处理拦截器,则对缓存数据做处理
                result = ((CacheInterceptor) clz.newInstance()).onCacheHit(result);
            }
            return result;
        }

        /*
         * 缓存未命中
         */
        // 执行方法获取返回结果
        result = joinPoint.proceed(joinPoint.getArgs());
        LOGGER.info("============ RedisAspect cache miss ==============");
        if (result != null) {
            Object resultValue = result;
            Class clz = cacheable.cacheInterceptor();
            if (!clz.isInterface()) {
                // 如果配置了缓存处理拦截器,则对缓存数据做处理
                resultValue = ((CacheInterceptor) clz.newInstance()).onCacheMiss(result);
            }
            // 缓存返回结果
            setValue(cacheable.cacheType(), cacheable.type(), rdbKey, resultValue, cacheable.expire());
        }

        return result;
    }

    /**
     * 处理CacheEvict注解
     *
     * @param joinPoint  切入点
     * @param cacheEvict CacheEvict注解
     * @return 注解方法上的执行结果
     */
    private Object evict(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        // 获取注解中的键值
        String rdbKey = getCacheEvictKey(joinPoint, cacheEvict);
        LOGGER.info("============ RedisAspect evict key {} ==============", rdbKey);

        // 执行方法获取返回结果
        Object result = joinPoint.proceed(joinPoint.getArgs());

        // 根据注解中的键移除缓存
        if (!StringUtils.isEmpty(rdbKey)) {
            redisTemplate.delete(rdbKey);
            LOGGER.info("============ RedisAspect evict() {} ==============", rdbKey);
        }
        return result;
    }

    /**
     * 配置环绕通知。
     * Cacheable注解,则表明该执行结果需要缓存;CacheEvict注解,则需要清除缓存。
     *
     * @param joinPoint 切入点
     * @return 执行结果
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("============ RedisAspect executed ==============");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 注册apache BeanUtils的日期转换器,BeanUtilsHashMapper.fromHash会使用到
        ConvertUtils.register(new DateConverter(), Date.class);
        if (method.isAnnotationPresent(Cacheable.class)) {
            return cache(joinPoint, method.getAnnotation(Cacheable.class));
        } else if (method.isAnnotationPresent(CacheEvict.class)) {
            return evict(joinPoint, method.getAnnotation(CacheEvict.class));
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}