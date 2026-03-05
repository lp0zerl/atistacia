package rule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.repository.TransactionRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Invest500Rule implements RecommendationRule {

    private final TransactionRepository transactionRepo;
    private static final String PRODUCT_NAME = "Invest 500";
    private static final UUID PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String PRODUCT_TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом...";

    @Override
    public Optional<Recommendation> evaluate(UUID userId) {
        // Правила:
        // 1. Пользователь использует как минимум один продукт с типом DEBIT.
        boolean hasDebit = transactionRepo.isUserOf(userId, "DEBIT");
        // 2. Пользователь не использует продукты с типом INVEST.
        boolean hasInvest = transactionRepo.isUserOf(userId, "INVEST");
        // 3. Сумма пополнений продуктов с типом SAVING больше 1000 ₽.
        long savingDeposits = transactionRepo.sumByUserProductTypeAndTransactionType(userId, "SAVING", "DEPOSIT");
        boolean savingMore1000 = savingDeposits > 1000;

        if (hasDebit && !hasInvest && savingMore1000) {
            return Optional.of(new Recommendation(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}