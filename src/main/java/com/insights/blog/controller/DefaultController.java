package com.insights.blog.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/success")
@AllArgsConstructor
public class DefaultController {
    @GetMapping
    public String defaultResponse() {
        return "Deployment Success!";
    }
}
