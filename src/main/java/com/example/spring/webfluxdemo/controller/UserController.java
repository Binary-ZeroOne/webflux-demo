package com.example.spring.webfluxdemo.controller;

import com.example.spring.webfluxdemo.domain.User;
import com.example.spring.webfluxdemo.repository.UserRepository;
import com.example.spring.webfluxdemo.utils.CheckUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
     * 以数组形式一次返回全部数据
     */
    @GetMapping(value = "/get_all")
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * 以SSE形式多次返回数据
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return userRepository.findAll();
    }

    /**
     * 添加用户
     *
     * @param user user
     * @return 新增的用户数据
     */
    @PostMapping("/save")
    public Mono<User> createUser(@Valid @RequestBody User user) {
        // 新增和修改都是save，有id是修改，无id则是新增
        user.setId(null);
        // 检查用户名是否符合规则
        CheckUtil.checkName(user.getName());

        return userRepository.save(user);
    }

    /**
     * 根据id删除用户
     *
     * @param id id
     * @return 用户存在返回200，不存在则返回404
     */
    @DeleteMapping("/del/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") String id) {
        // deleteById没有返回值，不能判断数据是否存在，所以一般来说实现删除逻辑时不能直接使用
        // userRepository.deleteById(id);

        // 当你要操作数据，并返回一个Mono这个时候使用flatMap。如果不操作数据，只是转换数据，并返回一个Mono时使用map
        return userRepository.findById(id).flatMap(user -> userRepository.delete(user)
                // 用户存在，删除成功
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                // 用户不存在，删除失败
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 修改用户数据
     *
     * @param id   id
     * @param user user
     * @return 用户数据存在时，返回200和修改后的数据，不存在则返回404
     */
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable("id") String id,
                                                 @Valid @RequestBody User user) {
        CheckUtil.checkName(user.getName());
        return userRepository.findById(id)
                // flatMap操作数据
                .flatMap(u -> {
                    BeanUtils.copyProperties(user, u);
                    u.setId(id);

                    return userRepository.save(u);
                })
                // map转换数据，用户数据存在，修改成功
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                // 用户数据不存在，修改失败
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据id查找用户
     *
     * @param id id
     * @return 用户存在返回200和用户信息，不存在则返回404
     */
    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable("id") String id) {
        return userRepository.findById(id)
                // 用户存在返回200和用户信息
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                // 不存在则返回404
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据年龄段查找用户数据
     *
     * @param start start
     * @param end   end
     * @return 以数组形式一次返回全部用户数据
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByAge(@PathVariable("start") int start,
                                @PathVariable("end") int end) {
        return userRepository.findByAgeBetween(start, end);
    }

    /**
     * 根据年龄段查找用户数据
     *
     * @param start start
     * @param end   end
     * @return 以SSE形式多次返回用户数据
     */
    @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindByAge(@PathVariable("start") int start,
                                      @PathVariable("end") int end) {
        return userRepository.findByAgeBetween(start, end);
    }

    /**
     * 查找20-45岁用户数据
     *
     * @return 以数组形式一次返回全部用户数据
     */
    @GetMapping("/old")
    public Flux<User> oldUser() {
        return userRepository.oldUser();
    }

    /**
     * 查找20-45岁用户数据
     *
     * @return 以SSE形式多次返回用户数据
     */
    @GetMapping(value = "/stream/old", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamOldUser() {
        return userRepository.oldUser();
    }
}
