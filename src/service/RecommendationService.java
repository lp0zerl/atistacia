package service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.model.entity.DynamicRuleEntity;
import ru.star.bank.recommendation.repository.DynamicRuleRepository;
import ru.star.bank.recommendation.repository.TransactionRepository;
import ru.star.bank.recommendation.rule.DynamicRecommendationRule;
import ru.star.bank.recommendation.rule.RecommendationRule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final List<RecommendationRule> fixedRules; // все бины, реализующие интерфейс
    private final DynamicRuleRepository dynamicRuleRepository;
    private final TransactionRepository transactionRepository;
    private final StatsService statsService;

    public List<Recommendation> getRecommendations(UUID userId) {
        List<Recommendation> result = new ArrayList<>();

        // Фиксированные правила
        for (RecommendationRule rule : fixedRules) {
            rule.evaluate(userId).ifPresent(result::add);
        }

        // Динамические правила
        List<DynamicRuleEntity> dynamicRules = dynamicRuleRepository.findAll();
        for (DynamicRuleEntity entity : dynamicRules) {
            DynamicRecommendationRule rule = new DynamicRecommendationRule(entity, transactionRepository);
            rule.evaluate(userId).ifPresent(rec -> {
                result.add(rec);
                statsService.increment(entity.getId());
            });
        }

        return result;
    }
}