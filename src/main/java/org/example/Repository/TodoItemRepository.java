package org.example.Repository;

import org.example.Entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface TodoItemRepository extends JpaRepository<ToDoItem, Long> {

    @Query("update ToDoItem set state = 'done', updateTime = current_timestamp where id in ?1")
    List<ToDoItem> finishById(Long[] ids);

}
