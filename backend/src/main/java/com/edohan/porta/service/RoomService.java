package com.edohan.porta.service;

import java.util.*;

public interface RoomService {
    List<Map<String, Object>> roomList(String keyword, String  region, String  genre);
    List<Map<String, Object>> myLikeList(String userId, String keyword, String  region, String  genre);
    List<Map<String, Object>> myRecordList(String userId, String keyword, String  region, String  genre);
    List<Map<String, Object>> newList();
    List<Map<String, Object>> rcmdList();
    List<Map<String, Object>> getRankList();
    Map<String, Object> getRoomDetail(int id);
    void saveRoom(Map<String, Object> map);
    void updateRoom(int id, Map<String, Object> map);
    void deleteRoom(int id);
    void incViewCount(int id);
    boolean checkIfLiked(String userId, int id);
    void addLikes(String userId, int roomId);
    void removeLikes(String userId, int roomId);
    void saveReview(Map<String, Object> map);
}
