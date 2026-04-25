package com.securechat.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/me")
    public String me() {
        return "Current user profile";
    }
}
