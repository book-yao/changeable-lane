package com.supcon.changeablelane.utils;

import java.util.Random;

/**
 * @author JWF
 * @Date 2018/11/13
 */
public class NumberUtil {
    private static  Random random = new Random();

    public static Integer getRandomNumber(){
        int i = random.nextInt(Double.valueOf((Math.random()*9+1)).intValue() + random.nextInt(1000000));
        return i;
    }

}
