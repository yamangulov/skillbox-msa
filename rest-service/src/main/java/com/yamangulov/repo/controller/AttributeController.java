package com.yamangulov.repo.controller;

import com.yamangulov.repo.service.AttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/attr", produces = MediaType.APPLICATION_JSON_VALUE)
@Api
public class AttributeController {
    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping("/city/{name}")
    @ApiOperation("Добавление города")
    public String addCity(@PathVariable String name) {
        return attributeService.addCity(name);
    }

    @PostMapping("/gender/{name}")
    @ApiOperation("Добавление пола")
    public String addGender(@PathVariable String name) {
        return attributeService.addGender(name);
    }

    @PostMapping("/skill/{name}")
    @ApiOperation("Добавление компетенции")
    public String addSkill(@PathVariable String name) {
        return attributeService.addSkill(name);
    }
}
