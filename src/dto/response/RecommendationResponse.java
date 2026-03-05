package dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.star.bank.recommendation.model.Recommendation;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private UUID userId;
    private List<Recommendation> recommendations;
}