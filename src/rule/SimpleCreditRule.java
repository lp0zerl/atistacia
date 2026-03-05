package rule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.repository.TransactionRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SimpleCreditRule implements RecommendationRule {

    private final TransactionRepository transactionRepo;
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_TEXT = "Откройте мир выгодных кредитов с нами!...";

    @Override
    public Optional<Recommendation> evaluate(UUID userId) {
        // 1. Пользователь не использует продукты с типом CREDIT.
        boolean hasCredit = transactionRepo.isUserOf(userId, "CREDIT");

        // 2. Сумма пополнений по DEBIT > сумма трат по DEBIT.
        long debitDeposits = transactionRepo.sumByUserProductTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        long debitWithdraw = transactionRepo.sumByUserProductTypeAndTransactionType(userId, "DEBIT", "WITHDRAW");
        boolean condition2 = debitDeposits > debitWithdraw;

        // 3. Сумма трат по DEBIT > 100000.
        boolean condition3 = debitWithdraw > 100000;

        if (!hasCredit && condition2 && condition3) {
            return Optional.of(new Recommendation(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}