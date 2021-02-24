package com.supcon.changeablelane.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author lidewen
 * @date 2018/8/20
 * @description JSON处理工具类，封装了Jackson的ObjectMapper对象
 */

@Slf4j
public class JsonUtil {
    /**
     * 核心处理对象，线程安全，可在tomcat的多线程环境中使用
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将Object对象转化为JSON字符串
     * @param object 待转化为JSON的对象
     * @return String 如果转化失败，返回空对象字符串："{}"
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("ToJson failed, object: " + object);
            log.error(e.getMessage(), e);
            return "{}";
        }

    }

    /**
     * 将JSON字符串转化为Object对象或空对象，用于JSON字符串是单个对象的情况
     * @param json 待转化的JSON字符串
     * @param clazz 待转化的对象类型
     * @return Optional<T> 如果转化成功，则为非空对象；如果转化失败，则为空对象
     */
    public static <T> Optional<T> fromJson(String json, Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (Exception e) {
            log.error("FromJson failed, string: " + json);
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * 将JSON字符串转化为Object对象或空对象，用于JSON字符串是对象的Map或List容器的情况
     * @param json 待转化的JSON字符串
     * @param valueTypeRef 用于定义List或Map中自定义的对象
     * @return Optional<T> 如果转化成功，则为非空对象；如果转化失败，则为空对象
     * @usage valueTypeRef = new TypeReference<Map<K, V>>(){}，或new TypeReference<List<E>>(){}
     */
    public static <T> Optional<T> fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return Optional.of(objectMapper.readValue(json, valueTypeRef));
        } catch (Exception e) {
            log.error("FromJson failed, string: " + json);
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
