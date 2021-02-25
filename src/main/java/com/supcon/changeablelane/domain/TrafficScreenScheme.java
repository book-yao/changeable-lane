package com.supcon.changeablelane.domain;

import lombok.Data;

/**
 * @Author caowenbo
 * @create 2021/2/24 14:56
 */
@Data
public class TrafficScreenScheme {

    private int id;

    /**
     * 中控设备编号
     */
    private String deviceCode;

    /**
     * 第一行文字内容
     */
    private String messageOne;

    /**
     * 第二行内容
     */
    private String messageTwo;

    /**
     * 颜色 屏上显示字体颜色 1红色 2 绿色 3 黄色
     */
    private String color;

    /**
     * 用于标记发屏，0取消发屏，1：发送本次内容
     */
    private int isSend;

    private int entranceId;

    private int acsId;

    private int schemeId;

    private int intersectionId;

    private String remark;
}
