package com.supcon.changeablelane.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 可变车道牌信息
 * @Author caowenbo
 * @create 2021/2/24 10:12
 */
@Data
public class VariableDriveway {

    private int single;

    private String statesStr;

    private int motorLaneId;

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
