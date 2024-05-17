package org.example.Repository;

import org.example.Entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface TodoItemRepository extends JpaRepository<ToDoItem, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE ToDoItem SET state = 'done', updateTime = current_timestamp WHERE id in ?1")
    void finishById(Long[] ids);

    //findAll(Sort.by(Sort.Direction.ASC, "id")) 手写排序

    /**
     * 按规则写名称，jpa会解析
     * @return
     */
    List<ToDoItem> findAllByOrderByIdAsc();

    /**
     * 按规则写名称，jpa会解析
     * @return
     */
    List<ToDoItem> findAllByOrderByIdDesc();
}
