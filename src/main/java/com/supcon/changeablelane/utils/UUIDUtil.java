package com.supcon.changeablelane.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JiangWangfa
 */
public class UUIDUtil {
    private static final Map<Integer,String> UUIDMAP = new ConcurrentHashMap<>();

    public static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static String getUUIDByEight(){
        return createUUID().substring(24);
    }

    public static String getUUIDBySixTeen(){
        return createUUID().substring(16);
    }

}
