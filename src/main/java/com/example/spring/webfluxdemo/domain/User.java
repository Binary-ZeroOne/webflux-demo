package com.example.spring.webfluxdemo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @program: webflux-demo
 * @description: User实体类
 * @author: 01
 * @create: 2018-10-04 20:25
 **/

@Data
@Document(collection = "user")
public class User {

    @Id
    private String id;

    private String name;

    private int age;
}
