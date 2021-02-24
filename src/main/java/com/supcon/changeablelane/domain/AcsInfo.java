package com.supcon.changeablelane.domain;

import lombok.Data;

/**
 * 可变车道信号机信息
 *
 * @Author caowenbo
 * @create 2021/2/24 9:54
 */
@Data
public class AcsInfo {
    private Integer acsId;
    private String name;
    private String ip;
    private double latitude;
    private double longitude;
    private int port;
    private int unitTwo;
    private int unitThree;
    private int deviceType;
    private boolean offline;
}
