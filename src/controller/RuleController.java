package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.star.bank.recommendation.dto.request.CreateRuleRequest;
import ru.star.bank.recommendation.dto.response.RuleListResponse;
import ru.star.bank.recommendation.dto.response.RuleResponse;
import ru.star.bank.recommendation.dto.response.StatsResponse;
import ru.star.bank.recommendation.service.DynamicRuleService;
import ru.star.bank.recommendation.service.StatsService;

import java.util.UUID;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class RuleController {

    private final DynamicRuleService ruleService;
    private final StatsService statsService;

    @PostMapping
    public ResponseEntity<RuleResponse> createRule(@RequestBody CreateRuleRequest request) {
        RuleResponse response = ruleService.createRule(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RuleListResponse> getAllRules() {
        RuleListResponse response = new RuleListResponse(ruleService.getAllRules());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        ruleService.deleteRule(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}