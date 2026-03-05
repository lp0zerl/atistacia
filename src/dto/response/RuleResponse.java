package dto.response;

import lombok.Data;
import ru.star.bank.recommendation.dto.QueryDto;

import java.util.List;
import java.util.UUID;

@Data
public class RuleResponse {
    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<QueryDto> rule;
}