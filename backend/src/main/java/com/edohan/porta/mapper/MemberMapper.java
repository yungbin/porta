package com.edohan.porta.mapper;

import java.util.*;

public interface MemberMapper {

    void insertMember(Map<String, String> member);

    Integer deleteMember(String userId);

    List<Map<String, Object>> memberList();

    Map<String, Object> getProfile(String userId);

    Map<String, Object> loginCheck(String userId, String password);
    
    Integer checkIdExists(String id);
}
