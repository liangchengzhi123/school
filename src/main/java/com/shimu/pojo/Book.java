package com.shimu.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {

    private Long id;

    private String name;

    private String author;

    private LocalDateTime publishTime;
    // 1. 可借出 2. 已经被借 3. 不可借出 4. 损坏
    private Integer status;
    // 位置编号
    private Integer position;
    // 分类
    private String tag;


}
