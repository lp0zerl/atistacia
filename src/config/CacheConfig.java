package config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.star.bank.recommendation.cache.CacheKey;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "userOf", "activeUserOf", "sumCompare", "sumCompareDepositWithdraw"
        );
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .recordStats();
    }

    // Отдельные кэши для ручного управления
    @Bean
    public Cache<CacheKey.UserOfKey, Boolean> userOfCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }

    @Bean
    public Cache<CacheKey.ActiveUserOfKey, Boolean> activeUserOfCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }

    @Bean
    public Cache<CacheKey.SumCompareKey, Long> sumCompareCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }

    @Bean
    public Cache<CacheKey.SumCompareDepositWithdrawKey, long[]> sumCompareDepositWithdrawCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }
}