package org.example.service.Impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.api.models.bot.ChatbotMessage;
import com.dingtalk.open.app.api.models.bot.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.example.Entity.ToDoItem;
import org.example.Repository.TodoItemRepository;
import org.example.common.Constant.Cmd;
import org.example.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class MessageHandlerImpl implements MessageHandler {

    @Resource
    private TodoItemRepository _repository;
    public String Handler(ChatbotMessage message) {
        MessageContent text = message.getText();
        String content = text.getContent();
        if (ObjectUtils.isEmpty(content)) {
            //do nothing
            return null;
        }
        if(content.trim().isEmpty()){
            //todo 返回所有

        }else if(content.trim().startsWith(Cmd.ADD)){
            //ADD
            ToDoItem toDoItem = new ToDoItem();
            String creater = message.getSenderNick();
            String reporter = message.getAtUsers().get(0).getStaffId();
            Date createTime = new Date(message.getCreateAt());
            String groups = message.getConversationTitle();
            toDoItem.setContent(content.trim().substring(Cmd.ADD.length()));
            toDoItem.setMessage(JSON.toJSONString(message));
            toDoItem.setCreateTime(createTime);
            toDoItem.setUpdateTime(createTime);
            toDoItem.setGroupName(groups);
            toDoItem.setCreater(creater);
            toDoItem.setRepoter(reporter);
            toDoItem.setOpenConversationId(message.getConversationId());
            _repository.save(toDoItem);

        } else if(content.trim().startsWith(Cmd.ALL)){
            //todo 返回所有
            return null;
        }else if(content.trim().startsWith(Cmd.DONE)){
            //todo 完成
            return null;
        }
        SimpleDateFormat timeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String reportMan = message.getAtUsers().get(0).getStaffId(); //这个是加密后的
        String toSend = String.format("召唤者: %s %n" +
                        "时间: %s %n" +
                        "群组: %s %n" +
                        "反馈人: %s %n",
                message.getSenderNick(),
                timeformatter.format(message.getCreateAt()),
                message.getConversationTitle(),
                reportMan);
        log.info("send back: {}", toSend);
        return toSend;
    }
}
