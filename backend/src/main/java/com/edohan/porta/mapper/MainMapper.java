package com.edohan.porta.mapper;

import java.util.List;
import java.util.Map;

public interface MainMapper {
    List<Map<String, Object>> highViewCountSelect();
    List<Map<String, Object>> recentPostsSelect();
    List<Map<String, Object>> topLikedPostsSelect();
}
