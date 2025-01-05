package com.edohan.porta.service;

import java.util.*;

public interface MailService {
    void sendVerificationEmail(Map<String, String> map);
    Map<String, Object> insertMailToken(Map<String, String> map);
    boolean checkMailToken(Map<String, String> map);
}
