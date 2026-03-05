package rule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.repository.TransactionRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TopSavingRule implements RecommendationRule {

    private final TransactionRepository transactionRepo;
    private static final String PRODUCT_NAME = "Top Saving";
    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_TEXT = "Откройте свою собственную «Копилку» с нашим банком!...";

    @Override
    public Optional<Recommendation> evaluate(UUID userId) {
        // 1. Пользователь использует как минимум один продукт с типом DEBIT.
        boolean hasDebit = transactionRepo.isUserOf(userId, "DEBIT");

        // 2. Сумма пополнений по DEBIT >= 50000 ИЛИ сумма пополнений по SAVING >= 50000.
        long debitDeposits = transactionRepo.sumByUserProductTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        long savingDeposits = transactionRepo.sumByUserProductTypeAndTransactionType(userId, "SAVING", "DEPOSIT");
        boolean condition2 = debitDeposits >= 50000 || savingDeposits >= 50000;

        // 3. Сумма пополнений по DEBIT > сумма трат по DEBIT.
        long debitWithdraw = transactionRepo.sumByUserProductTypeAndTransactionType(userId, "DEBIT", "WITHDRAW");
        boolean condition3 = debitDeposits > debitWithdraw;

        if (hasDebit && condition2 && condition3) {
            return Optional.of(new Recommendation(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}