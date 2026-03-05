package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "rule_stat")
@Data
@NoArgsConstructor
public class StatsEntity {
    @Id
    private UUID ruleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rule_id")
    private DynamicRuleEntity rule;

    @Column(nullable = false)
    private long count = 0;
}