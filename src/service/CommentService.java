package service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avito.dto.CommentDto;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.User;
import ru.avito.mapper.CommentMapper;
import ru.avito.repository.AdRepository;
import ru.avito.repository.CommentRepository;
import ru.avito.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByAdId(Long adId) {
        return commentRepository.findByAdId(adId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(Long adId, CommentDto commentDto, String userEmail) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setAd(ad);
        comment.setAuthor(author);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long adId, Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getAd().getId().equals(adId)) {
            throw new RuntimeException("Comment does not belong to this ad");
        }
        if (!comment.getAuthor().getEmail().equals(userEmail) && !isAdmin(userEmail)) {
            throw new AccessDeniedException("You can't delete this comment");
        }
        commentRepository.delete(comment);
    }

    private boolean isAdmin(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole() == Role.ADMIN;
    }
}