package com.edohan.porta.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edohan.porta.service.MainService;

@RestController
@RequestMapping("/api/main")
public class MainController {
    
    @Autowired
    private MainService service;

    @GetMapping("/list")
    public Map<String, Object> list() {

        List<Map<String, Object>> highViewCount = service.highViewCountSelect();
        List<Map<String, Object>> recentPosts = service.recentPostsSelect();
        List<Map<String, Object>> topLikedPosts = service.topLikedPostsSelect();

        return Map.of(
            "highViewCount", highViewCount,
            "recentPosts", recentPosts,
            "topLikedPosts", topLikedPosts
        );
    }
}
