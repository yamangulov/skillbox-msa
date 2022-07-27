package com.yamangulov.endpoint.controller;

import com.yamangulov.endpoint.entity.User;
import com.yamangulov.endpoint.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Api
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation("Создание пользователя")
    public String createUser(@RequestParam Map<String, String> requestParams) {
        return userService.creatUser(requestParams);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation("Обновление данных пользователя")
    public String updateUser(@RequestParam Map<String, String> requestParams, @PathVariable UUID id) {
        return userService.updateUser(requestParams, id);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation("Получение данных пользователя")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation("Удаление пользователя")
    public String deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/subscribe/{subscriber_id}/{subscribed_id}")
    @ApiOperation("Подписка пользователя на сообщения другого пользователя")
    public String subscribe(
            @PathVariable UUID subscriber_id,
            @PathVariable UUID subscribed_id) {
        return userService.subscribe(subscriber_id, subscribed_id);
    }

    @PostMapping("/unsubscribe/{subscriber_id}/{subscribed_id}")
    @ApiOperation("Отписка пользователя от сообщений другого пользователя")
    public String unsubscribe(
            @PathVariable UUID subscriber_id,
            @PathVariable UUID subscribed_id) {
        return userService.unsubscribe(subscriber_id, subscribed_id);
    }
}
