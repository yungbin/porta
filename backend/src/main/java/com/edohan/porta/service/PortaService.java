package com.edohan.porta.service;

import java.util.*;

public interface PortaService {
    List<Map<String, Object>> portaList(String keyword, String  region, String  genre);
    List<Map<String, Object>> myLikeList(String userId, String keyword, String  region, String  genre);
    List<Map<String, Object>> myRecordList(String userId, String keyword, String  region, String  genre);
    List<Map<String, Object>> newList();
    List<Map<String, Object>> rcmdList();
    List<Map<String, Object>> getRankList();
    Map<String, Object> getPortaDetail(int id);
    void savePorta(Map<String, Object> map);
    void updatePorta(int id, Map<String, Object> map);
    void deletePorta(int id);
    void incViewCount(int id);
    boolean checkIfLiked(String userId, int id);
    void addLikes(String userId, int portaId);
    void removeLikes(String userId, int portaId);
}
