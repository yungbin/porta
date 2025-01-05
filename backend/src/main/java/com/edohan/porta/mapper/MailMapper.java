package com.edohan.porta.mapper;

import java.util.*;

public interface MailMapper {

    void insertMailToken(Map<String, String> map);
    
    Integer checkMailToken(Map<String, String> map);

    Integer updateMailToken(Map<String, String> map);
}
