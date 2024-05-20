package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.Entity.ToDoItem;
import org.example.Repository.TodoItemRepository;
import org.example.controller.pojo.QueryBase;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("todo")
@RestController
@Api
public class ToDoItemController {

    private final TodoItemRepository _todoItemRepository;

    public ToDoItemController(TodoItemRepository todoItemRepository) {
        this._todoItemRepository = todoItemRepository;
    }

    @GetMapping("/all")
    @ApiOperation("all")
    public Iterable<ToDoItem> findAllEmployees() {
        return _todoItemRepository.findAll();
    }
    @GetMapping("/test1")
    @ApiOperation("test1")
    public String test(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    @ApiOperation("test2")
    @PostMapping("/test2")
    public Map<String, Date> test1(@RequestBody QueryBase query) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String input = simpleDateFormat.format(query.getDate());
        log.info(input);
        HashMap<String, Date> stringDateHashMap = new HashMap<>();
        stringDateHashMap.put("time", query.getDate());
        return stringDateHashMap;
    }
}
