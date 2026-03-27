package NotificationService;

import com.example.telegrambot.model.NotificationTask;
import com.example.telegrambot.repository.NotificationTaskRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    private final NotificationTaskRepository repository;

    // Паттерн для разбора сообщения: дата+время, пробелы, текст
    private static final Pattern PATTERN = Pattern.compile(
            "(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)"
    );

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public NotificationService(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Парсит входящий текст и сохраняет задачу, если формат верный.
     * @return true, если удалось сохранить, false — если формат неверен.
     */
    public boolean saveNotification(long chatId, String text) {
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            String dateTimeStr = matcher.group(1);   // "01.01.2022 20:00"
            String reminderText = matcher.group(3);  // "Сделать домашнюю работу"

            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, FORMATTER);
                NotificationTask task = new NotificationTask(chatId, reminderText, dateTime);
                repository.save(task);
                return true;
            } catch (Exception e) {
                // Если дата не распарсилась (например, неверный формат)
                return false;
            }
        }
        return false;
    }
}