package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * @author lidewen
 * @date 2018/8/27
 * @description 流向的相关配置信息
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movement {
    /**
     * 流向号
     */
    private Integer id;

    /**
     * 进口号
     */
    private Integer entranceId;

    /**
     * 最小绿灯时间
     */
    private Integer minGreenTime;

    /**
     * 最大绿灯时间
     */
    private Integer maxGreenTime;

    /**
     * 流向类型
     */
    private Integer movementType;

    @JsonGetter("number")
    public Integer getId() {
        return id;
    }

    @JsonSetter("number")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonGetter("entranceDirection")
    public Integer getEntranceId() {
        return entranceId;
    }

    @JsonSetter("entranceDirection")
    public void setEntranceId(Integer entranceId) {
        this.entranceId = entranceId;
    }

    @JsonGetter("minGreenTime")
    public Integer getMinGreenTime() {
        return minGreenTime;
    }

    @JsonSetter("minGreenTime")
    public void setMinGreenTime(Integer minGreenTime) {
        this.minGreenTime = minGreenTime;
    }

    @JsonGetter("maxGreenTime")
    public Integer getMaxGreenTime() {
        return maxGreenTime;
    }

    @JsonSetter("maxGreenTime")
    public void setMaxGreenTime(Integer maxGreenTime) {
        this.maxGreenTime = maxGreenTime;
    }

    @JsonGetter("movementType")
    public Integer getMovementType() {
        return movementType;
    }

    @JsonSetter("movementType")
    public void setMovementType(Integer movementType) {
        this.movementType = movementType;
    }
}
