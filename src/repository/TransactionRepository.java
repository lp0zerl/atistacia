package repository;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.star.bank.recommendation.cache.CacheKey;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final JdbcTemplate primaryJdbcTemplate;
    private final Cache<CacheKey.UserOfKey, Boolean> userOfCache;
    private final Cache<CacheKey.ActiveUserOfKey, Boolean> activeUserOfCache;
    private final Cache<CacheKey.SumCompareKey, Long> sumCompareCache;
    private final Cache<CacheKey.SumCompareDepositWithdrawKey, long[]> sumCompareDepositWithdrawCache;

    public boolean isUserOf(UUID userId, String productType) {
        CacheKey.UserOfKey key = new CacheKey.UserOfKey(userId, productType);
        return userOfCache.get(key, k -> {
            String sql = """
                SELECT COUNT(*) > 0
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
            """;
            Boolean result = primaryJdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(), productType);
            return result != null && result;
        });
    }

    public boolean isActiveUserOf(UUID userId, String productType) {
        CacheKey.ActiveUserOfKey key = new CacheKey.ActiveUserOfKey(userId, productType);
        return activeUserOfCache.get(key, k -> {
            String sql = """
                SELECT COUNT(*) >= 5
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
            """;
            Boolean result = primaryJdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(), productType);
            return result != null && result;
        });
    }

    public long sumByUserProductTypeAndTransactionType(UUID userId, String productType, String transactionType) {
        CacheKey.SumCompareKey key = new CacheKey.SumCompareKey(userId, productType, transactionType);
        return sumCompareCache.get(key, k -> {
            String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ? AND t.type = ?
            """;
            Long result = primaryJdbcTemplate.queryForObject(sql, Long.class, userId.toString(), productType, transactionType);
            return result == null ? 0L : result;
        });
    }

    public long[] sumDepositAndWithdrawByUserAndProductType(UUID userId, String productType) {
        CacheKey.SumCompareDepositWithdrawKey key = new CacheKey.SumCompareDepositWithdrawKey(userId, productType);
        return sumCompareDepositWithdrawCache.get(key, k -> {
            String sql = """
                SELECT 
                    COALESCE(SUM(CASE WHEN t.type = 'DEPOSIT' THEN t.amount ELSE 0 END), 0) as deposit_sum,
                    COALESCE(SUM(CASE WHEN t.type = 'WITHDRAW' THEN t.amount ELSE 0 END), 0) as withdraw_sum
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
            """;
            return primaryJdbcTemplate.queryForObject(sql, (rs, rowNum) -> new long[]{
                    rs.getLong(1), rs.getLong(2)
            }, userId.toString(), productType);
        });
    }

    // Метод для очистки кэша
    public void clearAllCaches() {
        userOfCache.invalidateAll();
        activeUserOfCache.invalidateAll();
        sumCompareCache.invalidateAll();
        sumCompareDepositWithdrawCache.invalidateAll();
    }
}