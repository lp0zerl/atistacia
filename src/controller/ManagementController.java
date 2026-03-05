package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.recommendation.dto.response.InfoResponse;
import ru.star.bank.recommendation.repository.TransactionRepository;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class ManagementController {

    private final TransactionRepository transactionRepository;
    private final BuildProperties buildProperties;

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        transactionRepository.clearAllCaches();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<InfoResponse> info() {
        InfoResponse response = new InfoResponse(
                buildProperties.getName(),
                buildProperties.getVersion()
        );
        return ResponseEntity.ok(response);
    }
}