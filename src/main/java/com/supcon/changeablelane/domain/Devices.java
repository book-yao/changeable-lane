package com.supcon.changeablelane.domain;

import lombok.Data;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 17:10
 */
@Data
public class Devices {

    List<AcsInfo> acsInfos;

    List<VariableDriveway> variableDriveways;

    List<TrafficScreen> trafficScreens;
}
