package com.edohan.porta.service;

import java.util.*;

public interface NoticeService {
    List<Map<String, Object>> noticeList();
    Map<String, Object> getNoticeDetail(int id);
    void saveNotice(String userId, String title, String content);
    void updateNotice(int id, String title, String content);
    void deleteNotice(int id);
} 