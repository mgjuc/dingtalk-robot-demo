package org.example.callback.chatbot;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dingtalk.open.app.api.models.bot.ChatbotMessage;
import com.dingtalk.open.app.api.models.bot.MessageContent;
import org.example.Entity.ToDoItem;
import org.example.service.MessageHandler;
import org.example.service.RobotGroupMessagesService;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDate.now;

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
                List<ToDoItem> items = messageHandler.Handler(message);
                if (!ObjectUtils.isEmpty(items)) {
                    String output = FormateOutput(items);
                    robotGroupMessagesService.send(openConversationId, output);
                }
            } catch (Exception e) {
                log.error("send group message by robot error:" + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("receive group message by robot error:" + e.getMessage(), e);
        }
        return new JSONObject();
    }

    /**
     * 格式化打印输出
     *
     * @param items
     * @return
     */
    private String FormateOutput(List<ToDoItem> items) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (items.isEmpty()) {
            return null;
        }
        List<ToDoItem> toDoItems = items.stream().filter(x -> ObjectUtils.isEmpty(x.getState())).collect(Collectors.toList());
        items.removeAll(toDoItems);
        StringBuilder stringBuilder = new StringBuilder();
        Date zeroClock = GetZeroClock(new Date());

        boolean hasdoneToday = false;
        if (!ObjectUtils.isEmpty(toDoItems)) {
            stringBuilder.append("待完成任务\r\n");
            for (ToDoItem item : toDoItems) {
                String content = item.getContent().length() > 30 ? item.getContent().substring(0, 30) : item.getContent();
                stringBuilder.append(String.format("%-4s | %-4s | %-30s %n", item.getId(), item.getState() == null ? "待办" : item.getState(), content));
            }
        }
        if (!ObjectUtils.isEmpty(items)) {
            for (ToDoItem item : items) {
                if (!GetZeroClock(item.getUpdateTime()).equals(zeroClock)) continue;
                if(!hasdoneToday){
                    hasdoneToday = true;
                    stringBuilder.append("已完成任务\r\n");
                }
                String content = item.getContent().length() > 30 ? item.getContent().substring(0, 30) : item.getContent();
                stringBuilder.append(String.format("%-4s | %-4s | %-30s %n", item.getId(), item.getState() == null ? "待办" : item.getState(), content));
            }
        }

        return stringBuilder.toString();
    }

    private Date GetZeroClock(Date current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
