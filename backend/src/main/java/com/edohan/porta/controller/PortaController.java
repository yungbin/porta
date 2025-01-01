package com.edohan.porta.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edohan.porta.service.PortaService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/porta")
public class PortaController {
    
    @Autowired
    private PortaService service;

    @GetMapping("/list")
    public List<Map<String, Object>> portaList(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "region", required = false) String region,
        @RequestParam(value = "genre", required = false) String genre) {
        return service.portaList(keyword, region, genre);
    }

    @GetMapping("/myLikes")
    public List<Map<String, Object>> myLikeList(
        @RequestParam(value = "userId", required = false) String userId,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "region", required = false) String region,
        @RequestParam(value = "genre", required = false) String genre) {
        return service.myLikeList(userId, keyword, region, genre);
    }

    @GetMapping("/myRecords")
    public List<Map<String, Object>> myRecordList(
        @RequestParam(value = "userId", required = false) String userId,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "region", required = false) String region,
        @RequestParam(value = "genre", required = false) String genre) {
        return service.myRecordList(userId, keyword, region, genre);
    }

    @GetMapping("/newList")
    public List<Map<String, Object>> newList() {
        return service.newList();
    }
    
    @GetMapping("/rcmdList")
    public List<Map<String, Object>> rcmdList() {
        return service.rcmdList();
    }
    
    @GetMapping("/rankList")
    public List<Map<String, Object>> getRankList() {
        return service.getRankList();
    }

    @GetMapping("/detail/{id}")
    public Map<String, Object> getPortaDetail(@PathVariable("id") int id, HttpServletRequest request, @RequestHeader(value = "User-Id", required = false) String userId) {
        Map<String, Object> portaDetail = service.getPortaDetail(id);
        service.incViewCount(id);
        boolean isLiked = checkIfLiked(userId, id);
        portaDetail.put("isLiked", isLiked);
        return portaDetail;
    }
    
    @PostMapping("/submit")
    public void savePorta(@RequestBody Map<String, Object> porta) {
        Map<String, Object> map = new HashMap<>();
        
        // 문자열 필드
        map.put("themeNm", (String) porta.get("THEME_NM"));
        map.put("portaNm", (String) porta.get("ROOM_NM"));
        map.put("regionCd", (String) porta.get("REGION_CD"));
        map.put("areaCd", (String) porta.get("AREA_CD"));
        map.put("genreCd", (String) porta.get("GENRE_CD"));
        map.put("content", (String) porta.get("CONTENT"));
        map.put("site", (String) porta.get("SITE"));
        map.put("imgPath", (String) porta.get("IMG_PATH"));
        map.put("newTheme", (String) porta.get("NEW_THEME"));
        map.put("rcmd", (String) porta.get("RCMD"));
        map.put("price", (String) porta.get("PRICE"));
        map.put("userId", (String) porta.get("userId"));
        
        // 숫자 필드 처리
        map.put("minParty", (Integer) porta.get("MIN_PARTY"));
        map.put("maxParty", (Integer) porta.get("MAX_PARTY"));
        map.put("runTime", (Integer) porta.get("RUN_TIME"));
        map.put("activiti", (Integer) porta.get("ACTIVITY"));
        map.put("diviceRatio", (Integer) porta.get("DEVICE_RATIO"));
        map.put("rating", (Double) porta.get("RATING"));
        map.put("level", (Integer) porta.get("LEVEL"));
        
        // 서비스 호출
        service.savePorta(map);
    }
    
    @PutMapping("/update/{id}")
    public void updatePorta(@PathVariable("id") int id, @RequestBody Map<String, Object> porta) {
        Map<String, Object> map = new HashMap<>();
        
        // 문자열 필드
        map.put("themeNm", (String) porta.get("THEME_NM"));
        map.put("portaNm", (String) porta.get("ROOM_NM"));
        map.put("regionCd", (String) porta.get("REGION_CD"));
        map.put("areaCd", (String) porta.get("AREA_CD"));
        map.put("genreCd", (String) porta.get("GENRE_CD"));
        map.put("content", (String) porta.get("CONTENT"));
        map.put("site", (String) porta.get("SITE"));
        map.put("imgPath", (String) porta.get("IMG_PATH"));
        map.put("newTheme", (String) porta.get("NEW_THEME"));
        map.put("rcmd", (String) porta.get("RCMD"));
        map.put("price", (String) porta.get("PRICE"));
        map.put("userId", (String) porta.get("userId"));
        
        // 숫자 필드 처리
        map.put("minParty", (Integer) porta.get("MIN_PARTY"));
        map.put("maxParty", (Integer) porta.get("MAX_PARTY"));
        map.put("runTime", (Integer) porta.get("RUN_TIME"));
        map.put("activiti", (Integer) porta.get("ACTIVITY"));
        map.put("diviceRatio", (Integer) porta.get("DEVICE_RATIO"));
        map.put("rating", (Double) porta.get("RATING"));
        map.put("level", (Integer) porta.get("LEVEL"));
        
        // 서비스 호출
        service.updatePorta(id, map);
    }
    
    // 아이디어 삭제
    @DeleteMapping("/PortaDelete/{id}")
    public void deletePorta(@PathVariable("id") int id) {
        service.deletePorta(id);
    }
    
    private boolean checkIfLiked(String userId, int themeId) {
        return service.checkIfLiked(userId,themeId);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<String> addLikes(@PathVariable("id") int id, @RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        if (userId != null && !userId.isEmpty() ) {
            service.addLikes(userId, id);
            return ResponseEntity.ok("Reaction added successfully.");
        } else {
            return ResponseEntity.badRequest().body("UserId is required.");
        }
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<String> removeLikes(@PathVariable("id") int id, @RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        if (userId != null && !userId.isEmpty() ) {
            service.removeLikes(userId, id);
            return ResponseEntity.ok("Reaction removeed successfully.");
        } else {
            return ResponseEntity.badRequest().body("UserId is required.");
        }
    }

}
