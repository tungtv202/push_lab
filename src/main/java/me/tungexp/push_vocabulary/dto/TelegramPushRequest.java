package me.tungexp.push_vocabulary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramPushRequest(@JsonProperty("chat_id") String chatId, String text) {
}
