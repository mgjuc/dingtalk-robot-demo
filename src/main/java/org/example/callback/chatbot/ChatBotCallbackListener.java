package org.example.callback.chatbot;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dingtalk.open.app.api.models.bot.ChatbotMessage;
import com.dingtalk.open.app.api.models.bot.MessageContent;
import org.example.service.MessageHandler;
import org.example.service.RobotGroupMessagesService;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * 机器人消息回调
 *
 * @author zeymo
 */
@Slf4j
@Component
public class ChatBotCallbackListener implements OpenDingTalkCallbackListener<ChatbotMessage, JSONObject> {
    private RobotGroupMessagesService robotGroupMessagesService;

    private MessageHandler messageHandler;
    @Autowired
    public ChatBotCallbackListener(RobotGroupMessagesService robotGroupMessagesService, MessageHandler messageHandler) {
        this.robotGroupMessagesService = robotGroupMessagesService;
        this.messageHandler = messageHandler;
    }

    /**
     * https://open.dingtalk.com/document/orgapp/the-application-robot-in-the-enterprise-sends-group-chat-messages
     *
     * @param message
     * @return
     */
    @Override
    public JSONObject execute(ChatbotMessage message) {
        try {
            String msgjson = JSON.toJSONString(message);
            log.info("message json: {}", msgjson);
            String openConversationId = message.getConversationId();
            try {
                //发送机器人消息
                String ret = messageHandler.Handler(message);
                if (ret != null) {
                    robotGroupMessagesService.send(openConversationId, ret);
                }
            } catch (Exception e) {
                log.error("send group message by robot error:" + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("receive group message by robot error:" + e.getMessage(), e);
        }
        return new JSONObject();
    }
}
