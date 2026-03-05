package cache;

import lombok.Value;

import java.util.UUID;

public final class CacheKey {

    @Value
    public static class UserOfKey {
        UUID userId;
        String productType;
    }

    @Value
    public static class ActiveUserOfKey {
        UUID userId;
        String productType;
    }

    @Value
    public static class SumCompareKey {
        UUID userId;
        String productType;
        String transactionType;
    }

    @Value
    public static class SumCompareDepositWithdrawKey {
        UUID userId;
        String productType;
    }
}