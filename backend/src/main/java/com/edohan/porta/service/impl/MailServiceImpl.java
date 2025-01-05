package com.edohan.porta.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edohan.porta.mapper.MailMapper;
import com.edohan.porta.service.MailService;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service("MailService")
public class MailServiceImpl implements MailService {
    
    @Autowired
    private MailMapper mapper;
    
    @Autowired
    private JavaMailSender mailSender;
    
    
    public void sendVerificationEmail(Map<String, String> map) {
        String subject = "이메일 인증 코드 : " + map.get("token");
        String text = "<html>" +
        "<body style='font-family: Arial, sans-serif;'>" +
        "<h2 style='color: #4CAF50;'>안녕하세요!</h2>" +
        "<p>귀하의 이메일 인증을 위한 코드는 다음과 같습니다:</p>" +
        "<h3 style='background-color: #f2f2f2; padding: 10px; border-radius: 5px;'>" +
        map.get("token") +
        "</h3>" +
        "<p>이 코드를 인증 페이지에 입력하여 이메일을 확인해 주세요.</p>" +
                "<p>감사합니다!</p>" +
                "<footer style='margin-top: 20px;'>" +
                "<p style='font-size: 12px; color: #777;'>이 메일은 자동으로 생성된 메일입니다. 답장하지 마세요.</p>" +
                "</footer>" +
                "</body>" +
                "</html>";
                
                MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject(subject, "utf-8");
            message.setText(text, "utf-8", "html");
            message.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(map.get("email")));
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Map<String, Object> insertMailToken(Map<String, String> map) {
        mapper.insertMailToken(map);
        return Collections.singletonMap("status", "succes");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkMailToken(Map<String, String> map) {
        Integer count = mapper.checkMailToken(map);
        mapper.updateMailToken(map);
    return count > 0;
    }
}
