package NotificationScheduler;

import com.example.telegrambot.model.NotificationTask;
import com.example.telegrambot.repository.NotificationTaskRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationScheduler {

    private final NotificationTaskRepository repository;
    private final TelegramBot telegramBot;

    public NotificationScheduler(NotificationTaskRepository repository, TelegramBot telegramBot) {
        this.repository = repository;
        this.telegramBot = telegramBot;
    }

    // Запуск каждую минуту (в 00 секунд)
    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotifications() {
        // Текущее время с отсечением секунд и наносекунд
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = repository.findByDateTime(now);

        for (NotificationTask task : tasks) {
            SendMessage message = new SendMessage(task.getChatId(), task.getMessage());
            try {
                telegramBot.execute(message);
            } catch (Exception e) {
                // Логируем ошибку, но продолжаем отправлять остальные
                e.printStackTrace();
            }
            // После отправки можно удалить задачу, чтобы не отправлять повторно
            repository.delete(task);
        }
    }
}