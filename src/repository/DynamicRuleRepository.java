package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.star.bank.recommendation.model.entity.DynamicRuleEntity;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRuleEntity, UUID> {
}