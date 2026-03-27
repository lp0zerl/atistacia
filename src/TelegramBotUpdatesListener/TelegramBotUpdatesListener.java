package TelegramBotUpdatesListener;

import com.example.telegrambot.service.NotificationService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            // Проверяем, что это текстовое сообщение
            if (update.message() != null && update.message().text() != null) {
                String messageText = update.message().text();
                long chatId = update.message().chat().id();

                if ("/start".equals(messageText)) {
                    sendMessage(chatId, "Привет! Я бот-напоминатель.\n" +
                            "Отправь мне сообщение в формате:\n" +
                            "01.01.2022 20:00 Сделать домашнюю работу");
                } else {
                    // Пытаемся распарсить и сохранить напоминание
                    boolean saved = notificationService.saveNotification(chatId, messageText);
                    if (!saved) {
                        sendMessage(chatId, "Неверный формат сообщения.\n" +
                                "Используйте: ДД.ММ.ГГГГ ЧЧ:ММ Текст напоминания");
                    } else {
                        sendMessage(chatId, "Напоминание сохранено!");
                    }
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        telegramBot.execute(sendMessage);
    }
}