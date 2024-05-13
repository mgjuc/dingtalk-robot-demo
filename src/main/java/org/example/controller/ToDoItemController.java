package org.example.controller;

import org.example.Entity.ToDoItem;
import org.example.Repository.TodoItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("todo")
@RestController
public class ToDoItemController {

    private final TodoItemRepository _todoItemRepository;

    public ToDoItemController(TodoItemRepository todoItemRepository) {
        this._todoItemRepository = todoItemRepository;
    }

    @GetMapping("/all")
    public Iterable<ToDoItem> findAllEmployees() {
        return _todoItemRepository.findAll();
    }
}
