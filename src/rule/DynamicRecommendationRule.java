package rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.model.entity.DynamicRuleEntity;
import ru.star.bank.recommendation.model.entity.RuleQueryEntity;
import ru.star.bank.recommendation.model.enums.CompareOperator;
import ru.star.bank.recommendation.model.enums.QueryType;
import ru.star.bank.recommendation.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DynamicRecommendationRule implements RecommendationRule {

    private final DynamicRuleEntity ruleEntity;
    private final TransactionRepository transactionRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<Recommendation> evaluate(UUID userId) {
        boolean allMatches = ruleEntity.getQueries().stream()
                .allMatch(q -> evaluateQuery(q, userId));
        if (allMatches) {
            return Optional.of(new Recommendation(
                    ruleEntity.getProductName(),
                    ruleEntity.getProductId(),
                    ruleEntity.getProductText()
            ));
        }
        return Optional.empty();
    }

    @SneakyThrows
    private boolean evaluateQuery(RuleQueryEntity query, UUID userId) {
        List<String> args = objectMapper.readValue(query.getArguments(), new TypeReference<>() {});
        boolean result = switch (query.getQueryType()) {
            case USER_OF -> transactionRepo.isUserOf(userId, args.get(0));
            case ACTIVE_USER_OF -> transactionRepo.isActiveUserOf(userId, args.get(0));
            case TRANSACTION_SUM_COMPARE -> {
                String productType = args.get(0);
                String transType = args.get(1);
                CompareOperator op = CompareOperator.fromSymbol(args.get(2));
                long constant = Long.parseLong(args.get(3));
                long sum = transactionRepo.sumByUserProductTypeAndTransactionType(userId, productType, transType);
                yield compare(sum, op, constant);
            }
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> {
                String productType = args.get(0);
                CompareOperator op = CompareOperator.fromSymbol(args.get(1));
                long[] sums = transactionRepo.sumDepositAndWithdrawByUserAndProductType(userId, productType);
                long deposit = sums[0];
                long withdraw = sums[1];
                yield compare(deposit, op, withdraw);
            }
        };
        return query.isNegate() ? !result : result;
    }

    private boolean compare(long left, CompareOperator op, long right) {
        return switch (op) {
            case GT -> left > right;
            case LT -> left < right;
            case EQ -> left == right;
            case GE -> left >= right;
            case LE -> left <= right;
        };
    }
}