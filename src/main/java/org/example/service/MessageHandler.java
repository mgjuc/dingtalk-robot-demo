package org.example.service;

import com.dingtalk.open.app.api.models.bot.ChatbotMessage;
import org.example.Entity.ToDoItem;

import java.util.List;


public interface MessageHandler {
    List<ToDoItem> Handler(ChatbotMessage message);
}
