package org.example.Entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "todoitems")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String creater;

    private String repoter;

    private String groupName;

    private Date createTime;

    private Date updateTime;
    @Column(length = 500)
    private String content;

    private String solution;

    private String state;

    private String openConversationId;
    @Column(length = 2048)
    private String message;

    private boolean isDeleted;

}
