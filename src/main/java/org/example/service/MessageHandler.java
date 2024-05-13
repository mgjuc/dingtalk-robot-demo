package org.example.service;

import com.dingtalk.open.app.api.models.bot.ChatbotMessage;


public interface MessageHandler {
    String Handler(ChatbotMessage message);
}
