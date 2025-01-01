package com.edohan.porta.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edohan.porta.mapper.NoticeMapper;
import com.edohan.porta.service.NoticeService;

@Service("NoticeService")
public class NoticeServiceImpl implements NoticeService {
    
    @Autowired
    private NoticeMapper mapper;
    
    @Override
    public List<Map<String, Object>> noticeList(){
        return mapper.noticeList();
    };

    @Override
    public Map<String, Object> getNoticeDetail(int id) {
        return mapper.getNoticeDetail(id);
    }

    @Override
    public void saveNotice(String userId, String title, String content) {
        mapper.saveNotice(userId, title, content);
    }

    @Override
    public void updateNotice(int id, String title, String content){
        mapper.updateNotice(id, title, content);
    }
    
    @Override
    public void deleteNotice(int id){
        mapper.deleteNotice(id);
    }
}
