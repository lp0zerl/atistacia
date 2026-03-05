package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.star.bank.recommendation.model.entity.StatsEntity;

import java.util.UUID;

@Repository
public interface StatsRepository extends JpaRepository<StatsEntity, UUID> {

    @Modifying
    @Query("UPDATE StatsEntity s SET s.count = s.count + 1 WHERE s.ruleId = :ruleId")
    void incrementCount(@Param("ruleId") UUID ruleId);

    @Modifying
    @Query("DELETE FROM StatsEntity s WHERE s.ruleId = :ruleId")
    void deleteByRuleId(@Param("ruleId") UUID ruleId);
}