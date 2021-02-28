package com.supcon.changeablelane.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author JiangWangfa
 * @date 2018/4/24
 * @description Gson的工具类
 */
public class GsonUtil {

    /**
     * Object转成json数据
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {

        if (object == null) {
            return "{}";
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * json转成对象
     *
     * @param str
     * @param clazz
     * @return
     */
    public static Object fromJson(String str, Class<?> clazz){
        return JSONObject.parseObject(str, clazz);
    }

}
