package org.example.controller.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class QueryBase {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
