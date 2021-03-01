package com.supcon.changeablelane.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Double longi;

    @JsonIgnore
    private Double lati;

    private int acsId;

    private int intersectionId;

    private int entranceId;


    private Double longitude;

    private Double latitude;

    private Double interSectionLongitude;

    private Double interSectionLatitude;

    public void setLongi(Double longi) {
        this.longi = longi;
        this.longitude = longi;
    }

    public void setLati(Double lati) {
        this.lati = lati;
        this.latitude = lati;
    }

    public TrafficScreenScheme singleScheme() {
        TrafficScreenScheme trafficScreenScheme = new TrafficScreenScheme();
        trafficScreenScheme.setAcsId(this.acsId);
        trafficScreenScheme.setDeviceCode(this.deviceCode);
        trafficScreenScheme.setIsSend(0);
        return trafficScreenScheme;
    }

    public TrafficScreenScheme createScheme() {
        TrafficScreenScheme trafficScreenScheme = new TrafficScreenScheme();
        trafficScreenScheme.setAcsId(this.acsId);
        trafficScreenScheme.setDeviceCode(this.deviceCode);
        trafficScreenScheme.setIsSend(1);
        trafficScreenScheme.setEntranceId(this.getEntranceId());
        trafficScreenScheme.setMessageOne("拥堵指数:停车指数");
        return trafficScreenScheme;
    }
}
