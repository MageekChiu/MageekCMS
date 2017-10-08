package cn.mageek.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class CacheConfiguration {

    //默认缓存
//    @Bean
//    public CacheManager cacheManager(){
//        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
//        simpleCacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("searches")));
//        return simpleCacheManager;
//    }

    //Guava 缓存
    @Primary //这个bean优先作为缓存 但是为啥不管用？ 还是用了redis的
    @Bean
    public CacheManager cacheManager(){
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager("searches");
        guavaCacheManager
                .setCacheBuilder(
                        CacheBuilder.newBuilder().softValues()
                        .expireAfterWrite(10, TimeUnit.MINUTES)//10分钟后失效
                );
        return guavaCacheManager;
    }

}
