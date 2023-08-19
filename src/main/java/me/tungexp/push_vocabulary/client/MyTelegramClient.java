package me.tungexp.push_vocabulary.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.tungexp.push_vocabulary.dto.TelegramPushRequest;

@Component
@RequiredArgsConstructor
public class MyTelegramClient {

    @Value("${telegram_bot_token:6302963671:AAFKnL8W31TjqbeEW4A_ZnenA6fShf1nNWc}")
    private String telegramBotToken;

    @Value("${telegram_chat_id:6275438853}")
    private String chatId;

    private final TelegramPushClient pushClient;

    public void push(String message) {
        pushClient.push(telegramBotToken, new TelegramPushRequest(chatId, message));
    }
}
