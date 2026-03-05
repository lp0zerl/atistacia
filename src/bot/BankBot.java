package bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.star.bank.recommendation.model.Recommendation;
import ru.star.bank.recommendation.service.RecommendationService;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankBot extends TelegramLongPollingBot {

    private final JdbcTemplate primaryJdbcTemplate; // для поиска пользователя по имени
    private final RecommendationService recommendationService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (messageText.startsWith("/recommend")) {
            String[] parts = messageText.split(" ", 2);
            if (parts.length < 2) {
                sendText(chatId, "Использование: /recommend <имя>");
                return;
            }
            String userName = parts[1].trim();
            handleRecommendCommand(chatId, userName);
        } else {
            sendText(chatId, "Привет! Я бот рекомендаций. Используй /recommend <имя> чтобы получить рекомендации.");
        }
    }

    private void handleRecommendCommand(long chatId, String userName) {
        // Ищем пользователя по имени (предположим, что в БД есть поля name и surname)
        // Для простоты будем искать по полному совпадению name + ' ' + surname
        String sql = "SELECT id, name, surname FROM users WHERE CONCAT(name, ' ', surname) = ?";
        List<UserInfo> users = primaryJdbcTemplate.query(sql, (rs, rowNum) -> new UserInfo(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("surname")
        ), userName);

        if (users.size() != 1) {
            sendText(chatId, "Пользователь не найден");
            return;
        }

        UserInfo user = users.get(0);
        List<Recommendation> recommendations = recommendationService.getRecommendations(user.id());

        StringBuilder response = new StringBuilder();
        response.append("Здравствуйте ").append(user.name()).append(" ").append(user.surname()).append("\n");
        response.append("Новые продукты для вас:\n");
        if (recommendations.isEmpty()) {
            response.append("Пока нет подходящих предложений.");
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                Recommendation rec = recommendations.get(i);
                response.append(i + 1).append(". ").append(rec.getName()).append("\n");
                response.append("   ").append(rec.getText()).append("\n\n");
            }
        }
        sendText(chatId, response.toString());
    }

    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message", e);
        }
    }

    private record UserInfo(UUID id, String name, String surname) {}
}