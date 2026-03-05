package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.star.bank.recommendation.dto.QueryDto;
import ru.star.bank.recommendation.dto.request.CreateRuleRequest;
import ru.star.bank.recommendation.dto.response.RuleResponse;
import ru.star.bank.recommendation.exception.RuleNotFoundException;
import ru.star.bank.recommendation.model.entity.DynamicRuleEntity;
import ru.star.bank.recommendation.model.entity.RuleQueryEntity;
import ru.star.bank.recommendation.model.entity.StatsEntity;
import ru.star.bank.recommendation.repository.DynamicRuleRepository;
import ru.star.bank.recommendation.repository.StatsRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DynamicRuleService {

    private final DynamicRuleRepository ruleRepository;
    private final StatsRepository statsRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public RuleResponse createRule(CreateRuleRequest request) {
        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setProductName(request.getProductName());
        rule.setProductId(request.getProductId());
        rule.setProductText(request.getProductText());

        List<RuleQueryEntity> queries = request.getRule().stream()
                .map(dto -> mapToQueryEntity(dto, rule))
                .collect(Collectors.toList());
        rule.setQueries(queries);

        // Создаём статистику с нулевым счётчиком
        StatsEntity stats = new StatsEntity();
        stats.setRule(rule);
        stats.setCount(0);
        rule.setStats(stats);

        DynamicRuleEntity saved = ruleRepository.save(rule);
        return mapToResponse(saved);
    }

    public List<RuleResponse> getAllRules() {
        return ruleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRule(UUID id) {
        if (!ruleRepository.existsById(id)) {
            throw new RuleNotFoundException("Rule not found with id: " + id);
        }
        // stats удалится каскадно
        ruleRepository.deleteById(id);
    }

    private RuleQueryEntity mapToQueryEntity(QueryDto dto, DynamicRuleEntity rule) {
        RuleQueryEntity entity = new RuleQueryEntity();
        entity.setRule(rule);
        entity.setQueryType(dto.getQuery());
        entity.setNegate(dto.isNegate());
        try {
            entity.setArguments(objectMapper.writeValueAsString(dto.getArguments()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize arguments", e);
        }
        return entity;
    }

    private RuleResponse mapToResponse(DynamicRuleEntity entity) {
        RuleResponse response = new RuleResponse();
        response.setId(entity.getId());
        response.setProductName(entity.getProductName());
        response.setProductId(entity.getProductId());
        response.setProductText(entity.getProductText());
        List<QueryDto> queries = entity.getQueries().stream()
                .map(this::mapToQueryDto)
                .collect(Collectors.toList());
        response.setRule(queries);
        return response;
    }

    private QueryDto mapToQueryDto(RuleQueryEntity entity) {
        QueryDto dto = new QueryDto();
        dto.setQuery(entity.getQueryType());
        dto.setNegate(entity.isNegate());
        try {
            dto.setArguments(objectMapper.readValue(entity.getArguments(), List.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize arguments", e);
        }
        return dto;
    }
}