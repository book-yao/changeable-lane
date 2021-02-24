package com.supcon.changeablelane.domain;

import lombok.Data;

/**
 * 交通诱导屏
 * @Author caowenbo
 * @create 2021/2/24 9:56
 */
@Data

public class TrafficScreen {

    private int id;

    private String deviceCode;

    private String deviceName;

    /**
     * 道路编码
     */
    private String roadCode;
    /**
     * 路段编码
     */
    private String edgeCode;

    /**
     * 地址
     */
    private String ipAddress;

    private int port;

    private Double longi;

    private Double lati;
}
