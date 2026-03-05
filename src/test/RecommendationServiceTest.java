package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.model.entity.DynamicRuleEntity;
import ru.star.bank.recommendation.repository.DynamicRuleRepository;
import ru.star.bank.recommendation.repository.TransactionRepository;
import ru.star.bank.recommendation.rule.RecommendationRule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private List<RecommendationRule> fixedRules;

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private RecommendationService service;

    @Test
    void shouldReturnEmptyListWhenNoRulesMatch() {
        UUID userId = UUID.randomUUID();
        when(fixedRules.stream()).thenReturn(List.<RecommendationRule>of().stream());
        when(dynamicRuleRepository.findAll()).thenReturn(List.of());

        List<Recommendation> result = service.getRecommendations(userId);
        assertThat(result).isEmpty();
    }

    // Дополнительные тесты...
}