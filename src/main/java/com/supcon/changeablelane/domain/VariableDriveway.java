package com.supcon.changeablelane.domain;

import lombok.Data;

/**
 * 可变车道牌信息
 * @Author caowenbo
 * @create 2021/2/24 10:12
 */
@Data
public class VariableDriveway {
    private int vlcId;

    private int devType;

    private int entranceId;

    private String ip;

    private double latitude;

    private double longitude;

    private String name;

    private int port;

    private  String primaryUnit;

    private String screen;

    private int intersectionId;

    private int unitThree;

    private int unitTwo;

}
