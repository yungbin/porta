package com.edohan.porta.mapper;

import java.util.List;
import java.util.Map;

public interface NoticeMapper {
    List<Map<String, Object>> noticeList();
    Map<String, Object> getNoticeDetail(int id);
    void saveNotice(String userId, String title, String content);
    void updateNotice(int id, String title, String content);
    void deleteNotice(int id);
}
