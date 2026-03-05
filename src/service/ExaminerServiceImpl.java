package service;

import com.example.exam.entity.Question;
import com.example.exam.exception.NotEnoughQuestionsException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class ExaminerServiceImpl implements ExaminerService {
    private final QuestionService questionService;

    public ExaminerServiceImpl(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public Collection<Question> getQuestions(int amount) {
        Collection<Question> allQuestions = questionService.getAll();
        if (amount > allQuestions.size()) {
            throw new NotEnoughQuestionsException("Запрошено больше вопросов, чем доступно. Доступно: " + allQuestions.size());
        }
        if (amount < 0) {
            throw new NotEnoughQuestionsException("Количество вопросов не может быть отрицательным");
        }
        Set<Question> result = new HashSet<>();
        while (result.size() < amount) {
            result.add(questionService.getRandomQuestion());
        }
        return result;
    }
}