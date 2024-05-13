package org.example.Repository;

import org.example.Entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoItemRepository extends JpaRepository<ToDoItem, Long> {
}
