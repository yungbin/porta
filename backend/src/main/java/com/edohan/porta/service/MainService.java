package com.edohan.porta.service;

import java.util.*;

public interface MainService {
    List<Map<String, Object>> highViewCountSelect();
    List<Map<String, Object>> recentPostsSelect();
    List<Map<String, Object>> topLikedPostsSelect();
} 