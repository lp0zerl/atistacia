package test;

import com.example.exam.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class JavaQuestionServiceTest {
    private JavaQuestionService questionService;

    @BeforeEach
    void setUp() {
        questionService = new JavaQuestionService();
    }

    @Test
    void shouldAddQuestionByString() {
        Question added = questionService.add("Q1", "A1");
        assertEquals("Q1", added.getQuestion());
        assertEquals("A1", added.getAnswer());
        assertTrue(questionService.getAll().contains(added));
    }

    @Test
    void shouldAddQuestionByObject() {
        Question q = new Question("Q2", "A2");
        Question added = questionService.add(q);
        assertSame(q, added);
        assertTrue(questionService.getAll().contains(q));
    }

    @Test
    void shouldNotDuplicateQuestions() {
        questionService.add("Q3", "A3");
        int sizeBefore = questionService.getAll().size();
        questionService.add("Q3", "A3");
        int sizeAfter = questionService.getAll().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void shouldRemoveExistingQuestion() {
        Question q = questionService.add("Q4", "A4");
        Question removed = questionService.remove(q);
        assertEquals(q, removed);
        assertFalse(questionService.getAll().contains(q));
    }

    @Test
    void shouldThrowWhenRemovingNonExisting() {
        Question q = new Question("Q5", "A5");
        assertThrows(NoSuchElementException.class, () -> questionService.remove(q));
    }

    @Test
    void shouldGetAllQuestions() {
        questionService.add("Q6", "A6");
        questionService.add("Q7", "A7");
        Collection<Question> all = questionService.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void shouldReturnRandomQuestion() {
        questionService.add("Q8", "A8");
        questionService.add("Q9", "A9");
        Question random = questionService.getRandomQuestion();
        assertNotNull(random);
        assertTrue(questionService.getAll().contains(random));
    }

    @Test
    void shouldThrowWhenRandomFromEmpty() {
        assertThrows(IllegalStateException.class, () -> questionService.getRandomQuestion());
    }

    @Test
    void shouldHandleEmptyCollectionOnGetAll() {
        Collection<Question> all = questionService.getAll();
        assertTrue(all.isEmpty());
    }
}