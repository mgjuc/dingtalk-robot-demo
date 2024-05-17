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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MessageHandlerImpl implements MessageHandler {

    @Resource
    private TodoItemRepository _repository;
    public List<ToDoItem> Handler(ChatbotMessage message) {
        List<ToDoItem> items = new ArrayList<>();
        MessageContent text = message.getText();
        String content = text.getContent();
        if (ObjectUtils.isEmpty(content)) {
            //do nothing
            return items;
        }
        content = content.trim();
        if(content.startsWith(Cmd.ADD)){
            //ADD
            ToDoItem toDoItem = new ToDoItem();
            String creater = message.getSenderNick();
            String reporter = message.getAtUsers().get(0).getStaffId();
            Date createTime = new Date(message.getCreateAt());
            String groups = message.getConversationTitle();
            toDoItem.setContent(content.substring(Cmd.ADD.length()));
            toDoItem.setMessage(JSON.toJSONString(message));
            toDoItem.setCreateTime(createTime);
            toDoItem.setUpdateTime(createTime);
            toDoItem.setGroupName(groups);
            toDoItem.setCreater(creater);
            toDoItem.setRepoter(reporter);
            toDoItem.setOpenConversationId(message.getConversationId());
            _repository.save(toDoItem);
        } else if(content.startsWith(Cmd.DONE)){
            //完成
            content = content.substring(Cmd.DONE.length()).trim();
            String[] args = content.split(" ");
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                if(ObjectUtils.isEmpty(args[i])) continue;
                Long id = Long.parseLong(args[i]);
                ids.add(id);
            }
//            long[] arr = ids.stream().mapToLong(l -> l).toArray();
            Long[] arr = ids.toArray(new Long[0]);
            _repository.finishById(arr);
        }
/*        else if(content.startsWith(Cmd.ALL)){

        }else if(content.isEmpty()){

        }else{

        }*/
        items = _repository.findAllByOrderByIdAsc();
        return items;
    }
}
