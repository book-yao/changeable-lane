package com.supcon.changeablelane.domain;

import lombok.Data;

import java.util.List;

/**
 * 可变车道协同体方案
 * @Author caowenbo
 * @create 2021/2/24 14:52
 */
@Data
public class Scheme {

    private int schemeId;

    /**
     * 可变车道牌方案
     */
    private List<VariableLaneDTO> variableLaneDTOSchemes;

    /**
     * 诱导屏方案信息
     */
    private List<TrafficScreenScheme> trafficScreenSchemes;

    /**
     * 信号机方案
     */
    //private List<AcsOutput> acsOutputs;
}
