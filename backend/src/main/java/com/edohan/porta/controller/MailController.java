package com.edohan.porta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.edohan.porta.service.MailService;
import java.util.*;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService service;

    @PostMapping("/send")
    public String sendVerificationMail(@RequestBody Map<String, String> request) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", request.get("userId"));
        map.put("email", request.get("email"));
        map.put("token", generateRandomToken());
        service.sendVerificationEmail(map);
        service.insertMailToken(map);

        return "인증 이메일이 전송되었습니다.";
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyEmail(@RequestBody Map<String, String> request) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", request.get("userId"));
        map.put("email", request.get("email"));
        map.put("token", request.get("token"));
        boolean rs = service.checkMailToken(map);
        Map<String, Object> response = new HashMap<>();

        response.put("succes", rs);
        return response;
    }

    public String generateRandomToken() {
    SecureRandom random = new SecureRandom();
    int randomNumber = random.nextInt(1000000);
    return String.format("%06d", randomNumber);
    }
}
