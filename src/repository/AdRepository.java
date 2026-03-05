package ru.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avito.entity.Ad;
import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByAuthorId(Long authorId);
}