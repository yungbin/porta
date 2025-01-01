package com.edohan.porta.service;

import java.util.*;

public interface MemberService {
    Map<String, Object> register(Map<String, String> map);
    Map<String, Object> login(String userId, String password);
    List<Map<String, Object>> memberList();
    Map<String, Object> getProfile(String userId);
    boolean deleteMember(String userId);
    boolean checkIdExists(String id);
}
