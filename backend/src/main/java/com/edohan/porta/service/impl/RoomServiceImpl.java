package com.edohan.porta.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edohan.porta.mapper.RoomMapper;
import com.edohan.porta.service.RoomService;

@Service("RoomService")
public class RoomServiceImpl implements RoomService {
    
    @Autowired
    private RoomMapper mapper;
    
    @Override
    public List<Map<String, Object>> roomList(String keyword, String region, String genre) {
        return mapper.roomList(keyword, region, genre);
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
    public Map<String, Object> getRoomDetail(int id) {
        Map<String, Object> roomDetail = mapper.getRoomDetail(id);
        List<Map<String, Object>> reviews = mapper.getRoomReviews(id);
        roomDetail.put("reviews", reviews);

        return roomDetail;
    }
    
    @Override
    public List<Map<String, Object>> getRankList() {
        return mapper.getRankList();
    }
    
    @Override
    public void saveRoom(Map<String, Object> map) {
        mapper.saveRoom(map);
    }

    @Override
    public void updateRoom(int id,Map<String, Object> map) {
        mapper.updateRoom(id, map);
    }

    @Override
    public void deleteRoom(int id) {
        mapper.deleteRoom(id);
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

    @Override
public void saveReview(Map<String, Object> map) {
    mapper.saveReview(map);
}

}
