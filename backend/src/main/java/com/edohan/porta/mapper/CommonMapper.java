package com.edohan.porta.mapper;

import java.util.*;

public interface CommonMapper {
    List<Map<String, Object>> codeList(String type);
    
    List<Map<String, Object>> areaCdList(String regionCode);
}
