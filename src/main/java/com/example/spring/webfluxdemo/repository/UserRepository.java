package com.example.spring.webfluxdemo.repository;

import com.example.spring.webfluxdemo.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: webflux-demo
 * @description: user dao层接口
 * @author: 01
 * @create: 2018-10-04 20:27
 **/

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
