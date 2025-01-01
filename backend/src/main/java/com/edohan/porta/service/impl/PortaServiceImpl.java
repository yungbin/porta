package com.edohan.porta.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edohan.porta.mapper.PortaMapper;
import com.edohan.porta.service.PortaService;

@Service("PortaService")
public class PortaServiceImpl implements PortaService {
    
    @Autowired
    private PortaMapper mapper;
    
    @Override
    public List<Map<String, Object>> portaList(String keyword, String region, String genre) {
        return mapper.portaList(keyword, region, genre);
    }

    @Override
    public List<Map<String, Object>> myLikeList(String userId, String keyword, String region, String genre) {
        return mapper.myLikeList(userId, keyword, region, genre);
    }

    @Override
    public List<Map<String, Object>> myRecordList(String userId, String keyword, String region, String genre) {
        return mapper.myRecordList(userId, keyword, region, genre);
    }

    @Override
    public List<Map<String, Object>> newList() {
        return mapper.newList();
    }
    
    @Override
    public List<Map<String, Object>> rcmdList() {
        return mapper.rcmdList();
    }
    
    @Override
    public Map<String, Object> getPortaDetail(int id) {
        Map<String, Object> portaDetail = mapper.getPortaDetail(id);
        
        return portaDetail;
    }
    
    @Override
    public List<Map<String, Object>> getRankList() {
        return mapper.getRankList();
    }
    
    @Override
    public void savePorta(Map<String, Object> map) {
        mapper.savePorta(map);
    }

    @Override
    public void updatePorta(int id,Map<String, Object> map) {
        mapper.updatePorta(id, map);
    }

    @Override
    public void deletePorta(int id) {
        mapper.deletePorta(id);
    }

    @Override
    public void incViewCount(int id) {
        mapper.incViewCount(id);
    }

    @Override
    public boolean checkIfLiked(String userId, int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", id);

        Integer count = mapper.checkIfLiked(params);
    return count != null && count > 0;
    }

    @Override
    public void addLikes(String userId, int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", id);

        mapper.addLikes(params);
    }

    @Override
    public void removeLikes(String userId, int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", id);

        // 반응 삭제
        mapper.removeLikes(params);
    }

}
