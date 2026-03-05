package ru.star.bank.recommendation.controller;

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

package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.recommendation.dto.response.RecommendationResponse;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.service.RecommendationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{user_id}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable("user_id") UUID userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendations(userId);
        RecommendationResponse response = new RecommendationResponse(userId, recommendations);
        return ResponseEntity.ok(response);
    }
}