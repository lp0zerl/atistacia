package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avito.entity.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAdId(Long adId);
    List<Comment> findByAuthorId(Long authorId);
}