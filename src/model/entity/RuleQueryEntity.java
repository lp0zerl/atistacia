package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.star.bank.recommendation.model.enums.QueryType;

import java.util.List;

@Entity
@Table(name = "rule_query")
@Data
@NoArgsConstructor
public class RuleQueryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private DynamicRuleEntity rule;

    @Enumerated(EnumType.STRING)
    @Column(name = "query_type", nullable = false)
    private QueryType queryType;

    @Column(name = "arguments", nullable = false)
    private String arguments; // JSON строка, например ["DEBIT", ">"]

    @Column(name = "negate", nullable = false)
    private boolean negate;

    // Вспомогательный метод для получения аргументов как списка
    public List<String> getArgumentsAsList() {
        // Использовать Jackson для десериализации JSON
        // Упрощённо: если формат всегда простой, можно хранить как строку с разделителями
        // Для простоты используем здесь Jackson ObjectMapper
        // Но в коде лучше добавить @Convert или @Type(JsonType)
        throw new UnsupportedOperationException("Implement JSON conversion");
    }
}