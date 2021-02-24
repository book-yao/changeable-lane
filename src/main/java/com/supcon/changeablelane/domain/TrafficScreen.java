package com.supcon.changeablelane.domain;

import lombok.Data;

import java.time.LocalDateTime;

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

    private int deviceStatus;

    private LocalDateTime endTime;

    private String ipAddress;

    private int manufacturer;

    private String picName;

    private int port;

    private int screenType;

    private LocalDateTime setTime;

    private int timerIsok;

    private int timerState;
}
