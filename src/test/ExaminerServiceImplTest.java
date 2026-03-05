package test;

import com.example.exam.entity.Question;
import com.example.exam.exception.NotEnoughQuestionsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {
    @Mock
    private QuestionService questionService;
    private ExaminerServiceImpl examinerService;

    @BeforeEach
    void setUp() {
        examinerService = new ExaminerServiceImpl(questionService);
    }

    @Test
    void shouldReturnRequestedAmountOfUniqueQuestions() {
        Set<Question> allQuestions = new HashSet<>();
        Question q1 = new Question("Q1", "A1");
        Question q2 = new Question("Q2", "A2");
        Question q3 = new Question("Q3", "A3");
        allQuestions.add(q1);
        allQuestions.add(q2);
        allQuestions.add(q3);

        when(questionService.getAll()).thenReturn(allQuestions);
        when(questionService.getRandomQuestion())
                .thenReturn(q1, q2, q3, q1);

        Collection<Question> result = examinerService.getQuestions(3);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(allQuestions));
    }

    @Test
    void shouldThrowWhenAmountExceedsAvailable() {
        Set<Question> allQuestions = new HashSet<>();
        allQuestions.add(new Question("Q1", "A1"));
        when(questionService.getAll()).thenReturn(allQuestions);
        assertThrows(NotEnoughQuestionsException.class, () -> examinerService.getQuestions(2));
    }

    @Test
    void shouldThrowWhenAmountNegative() {
        when(questionService.getAll()).thenReturn(new HashSet<>());
        assertThrows(NotEnoughQuestionsException.class, () -> examinerService.getQuestions(-1));
    }

    @Test
    void shouldReturnEmptyCollectionWhenAmountZero() {
        when(questionService.getAll()).thenReturn(new HashSet<>());
        Collection<Question> result = examinerService.getQuestions(0);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleDuplicatesInRandom() {
        Set<Question> allQuestions = new HashSet<>();
        Question q1 = new Question("Q1", "A1");
        Question q2 = new Question("Q2", "A2");
        allQuestions.add(q1);
        allQuestions.add(q2);

        when(questionService.getAll()).thenReturn(allQuestions);
        when(questionService.getRandomQuestion())
                .thenReturn(q1, q1, q2);

        Collection<Question> result = examinerService.getQuestions(2);
        assertEquals(2, result.size());
        assertTrue(result.contains(q1));
        assertTrue(result.contains(q2));
        verify(questionService, times(3)).getRandomQuestion();
    }
}