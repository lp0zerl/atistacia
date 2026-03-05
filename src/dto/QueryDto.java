package dto;

import lombok.Data;
import ru.star.bank.recommendation.model.enums.QueryType;

import java.util.List;

@Data
public class QueryDto {
    private QueryType query;
    private List<String> arguments;
    private boolean negate;
}