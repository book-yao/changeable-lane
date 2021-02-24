package com.supcon.changeablelane.domain;

import lombok.Data;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 9:51
 */
@Data
public class ChangeableLaneArea {

    private Integer teamId;

    private String teamName;

    private String rings;

    //1已锁定 2 未锁定
    private Integer status;

    /**
     * 信号机列表
     */
    private List<AcsInfo> acsInfo;

    private List<TrafficScreen> trafficScreens;

    private List<Scheme> schemes;
}
