package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import lombok.Data;

import java.util.List;

/**
 * @author lidewen
 * @date 2018/8/27
 * @description 信号机单点特征参数
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcsConfigInfoDTO {
    /**
     * 信号机id
     */
    private Integer acsId;

    /**
     * 流向信息
     */
    private List<Movement> movements;

    /**
     * 检测器（车道）信息
     */
    private List<Detector> detectors;

    /**
     * 相位方案信息
     */
    private List<PhaseScheme> phaseSchemes;


    @JsonGetter("id")
    public Integer getAcsId() {
        return acsId;
    }

    @JsonSetter("acsId")
    public void setAcsId(Integer acsId) {
        this.acsId = acsId;
    }

    @JsonGetter("movements")
    public List<Movement> getMovements() {
        return movements;
    }

    @JsonSetter("movements")
    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }

    @JsonGetter("detectors")
    public List<Detector> getDetectors() {
        return detectors;
    }

    @JsonSetter("detectors")
    public void setDetectors(List<Detector> detectors) {
        this.detectors = detectors;
    }

    @JsonGetter("phaseSchemes")
    public List<PhaseScheme> getPhaseSchemes() {
        return phaseSchemes;
    }

    @JsonSetter("phaseSchemes")
    public void setPhaseSchemes(List<PhaseScheme> phaseSchemes) {
        this.phaseSchemes = phaseSchemes;
    }
}
