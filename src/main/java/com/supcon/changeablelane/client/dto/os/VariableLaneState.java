package com.supcon.changeablelane.client.dto.os;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.supcon.changeablelane.client.dto.VariableLaneStateDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangfei
 * @date 2020/2/21
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableLaneState {

    /**
     * 信号机id
     */
    private Integer acsId;

    /**
     * 进口id
     */
    private Integer entranceId;

    /**
     * 车道id
     */
    private Integer laneId;

    /**
     * 车道类型：1-进口车道 2-出口车道
     */
    private Integer laneType;

    /**
     * 可变车道状态：1-在线 2-离线
     */
    private Integer state;

    /**
     * 可变车道方向信息：1-左 2-直 3-右
     */
    private Integer status;

    @JsonGetter("entranceId")
    public Integer getEntranceId() {
        return entranceId;
    }

    @JsonSetter("entrance_id")
    public void setEntranceId(Integer entranceId) {
        this.entranceId = entranceId;
    }

    @JsonGetter("laneId")
    public Integer getLaneId() {
        return laneId;
    }

    @JsonSetter("lane_id")
    public void setLaneId(Integer laneId) {
        this.laneId = laneId;
    }

    @JsonGetter("laneType")
    public Integer getLaneType() {
        return laneType;
    }

    @JsonSetter("lane_type")
    public void setLaneType(Integer laneType) {
        this.laneType = laneType;
    }

    public VariableLaneStateDTO transform2VariableLaneStateDTO() {
        VariableLaneStateDTO dto = new VariableLaneStateDTO();
        dto.setAcsId(this.getAcsId());
        dto.setEntranceId(this.getEntranceId());
        dto.setLaneId(this.getLaneId());
        dto.setState(this.getState());
        dto.setStatus(this.getStatus());
        return dto;
    }
}
