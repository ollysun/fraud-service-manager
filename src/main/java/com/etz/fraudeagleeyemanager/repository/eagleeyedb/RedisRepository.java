package com.etz.fraudeagleeyemanager.repository.eagleeyedb;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;

import com.etz.fraudeagleeyemanager.util.JsonConverter;


public interface RedisRepository<T, U> {

    public void create(T model);

    public Map<U, T> findAll();

    public T findById(U id);

    public void update(T model);

    public void delete(U id);

    public default String toJsonString(T model) {
        return JsonConverter.objectToJson(model);
    }

    public default T convertResponseToEntity(Object obj, Class<?> clazz) {
        return JsonConverter.jsonToObject(String.valueOf(obj), clazz);
    }

    public default Map<U, T> convertResponseToEntityMap(Map<U, Object> objMap, Class<?> clazz) {
        Map<U, T> response = new HashMap<>();
        objMap.entrySet().stream().forEach(entry -> response.put(entry.getKey(), convertResponseToEntity(entry.getValue(), clazz)));
        return response;
    }

    public default Set<U> scanKeys(String keyPatternToMatch, HashOperations<String, U, Object> hashOperations, String key) {
        Set<U> foundKeys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(keyPatternToMatch).count(Integer.MAX_VALUE).build();
        Cursor<Map.Entry<U, Object>> cursor = hashOperations.scan(key, options);
        while (cursor.hasNext()) {
            //foundKeys.add(new String(cursor.next().getKey()))
            foundKeys.add(cursor.next().getKey());
        }
        try {
            cursor.close();
        } catch (IOException e) {}
        return foundKeys;
    }
}
