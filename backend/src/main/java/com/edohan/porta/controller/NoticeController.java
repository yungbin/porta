package com.edohan.porta.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edohan.porta.service.NoticeService;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    
    @Autowired
    private NoticeService service;

    @GetMapping("/list")
    public List<Map<String, Object>> noticeList() {
        return service.noticeList();
    }

    @GetMapping("/detail/{id}")
    public Map<String, Object> getNoticeDetail(@PathVariable("id") int id) {
        return service.getNoticeDetail(id);
    }

    @PostMapping("/submit")
    public void saveNotice(@RequestBody Map<String, Object> idea) {
        String userId = (String) idea.get("userId"); 
        String title = (String) idea.get("title");
        String content = (String) idea.get("content");
        service.saveNotice(userId, title, content);
    }

    @PutMapping("/update/{id}")
    public void updateNotice(@PathVariable("id") int id, @RequestBody Map<String, String> notice) {
        String title = notice.get("TITLE");
        String content = notice.get("CONTENT");
        service.updateNotice(id, title, content);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotice(@PathVariable("id") int id) {
        service.deleteNotice(id);
    }
}
