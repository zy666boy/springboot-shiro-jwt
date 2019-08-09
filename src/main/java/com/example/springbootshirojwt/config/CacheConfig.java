package com.example.springbootshirojwt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * 缓存配置类
 * CachingConfigurerSupport是spring封装好的缓存类，继承后直接重写相关方法即可，也可不继承,按相关方法返回的bean，直接写类似的方法返回。
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    private static final Logger lg = LoggerFactory.getLogger(CacheConfig.class);
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    /**
     * 设置自动key的生成规则，配置spring boot的注解(@Cacheable的key属性可以选择此bean)，进行方法级别的缓存
     *
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        // 使用：进行分割，可以很多显示出层级关系
        // 这里其实就是new了一个KeyGenerator对象，只是这是lambda表达式的写法,最下面是不使用lambda的写法,涉及到lambda对内部类的简写
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(":" + String.valueOf(obj));
            }
            String rsToUse = String.valueOf(sb);
            return rsToUse;
        };

       /* return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };*/
    }

    /**
     * 缓存管理器，在这里我们可以缓存的整体过期时间什么的，默认没有配置
     *
     * @return
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(jedisConnectionFactory);
        //添加缓存块
        Set<String>cacheNames=new HashSet<String>();
        cacheNames.add("MenuInfo");
        cacheNames.add("UserInfo");
        builder.initialCacheNames(cacheNames);
        //设置缓存过期时间(也可以通过RedisUtil指定某个key的缓存过期时间)
        //defaultCacheConfig()返回的对象能设置许多缓存规则，Duration里有许多过期时间单位
        //不过这好像并没有起作用
        builder.cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
        RedisCacheManager cacheManager=builder.build();
        return cacheManager;
    }

    /**
     *  异常处理，当Redis发生异常时，打印日志，但是程序正常走
     * @return
     */
    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        lg.info("初始化 -> [{}]", "Redis CacheErrorHandler");
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                lg.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }
            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                lg.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }
            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)    {
                lg.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }
            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                lg.error("Redis occur handleCacheClearError：", e);
            }
        };
        return cacheErrorHandler;
    }
}
