package com.example.spring.webfluxdemo.controller;

import com.example.spring.webfluxdemo.domain.User;
import com.example.spring.webfluxdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @program: webflux-demo
 * @description: User 接口
 * @author: 01
 * @create: 2018-10-04 20:29
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 返回全部数据
     */
    @GetMapping(value = "/get_all")
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * 流式返回数据
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return userRepository.findAll();
    }
}
