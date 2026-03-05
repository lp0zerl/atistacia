package service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.star.bank.recommendation.dto.response.StatsResponse;
import ru.star.bank.recommendation.model.entity.DynamicRuleEntity;
import ru.star.bank.recommendation.repository.DynamicRuleRepository;
import ru.star.bank.recommendation.repository.StatsRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final DynamicRuleRepository ruleRepository;

    @Transactional
    public void increment(UUID ruleId) {
        statsRepository.incrementCount(ruleId);
    }

    public StatsResponse getStats() {
        List<DynamicRuleEntity> rules = ruleRepository.findAll();
        List<StatsResponse.StatItem> items = rules.stream()
                .map(rule -> new StatsResponse.StatItem(
                        rule.getId(),
                        rule.getStats() != null ? rule.getStats().getCount() : 0))
                .collect(Collectors.toList());
        return new StatsResponse(items);
    }
}