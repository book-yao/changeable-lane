package com.supcon.changeablelane.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author caowenbo
 * @create 2021/2/24 9:51
 */
@Data
public class ChangeableLaneArea {

    private Integer areaId;

    private String areaName;

    private String rings;

    //1已锁定 2 未锁定
    private Integer status;

    /**
     * 信号机列表
     */
    @JsonIgnore
    private List<AcsInfo> acsInfo;

    @JsonIgnore
    private List<TrafficScreen> trafficScreens;

    @JsonIgnore
    private List<Scheme> schemes;

    public void handlerStatus(ChangeableLaneLock changeableLaneLock) {
        if(Objects.isNull(changeableLaneLock)
                ||Objects.isNull(changeableLaneLock.getStartTime())){
            this.setStatus(2);
        }
        LocalDateTime startTime = changeableLaneLock.getStartTime();
        if(Objects.nonNull(changeableLaneLock.getLockHour())){
            startTime = startTime.plusHours(changeableLaneLock.getLockHour());
        }
        if(Objects.nonNull(changeableLaneLock.getLockMinute())){
            startTime = startTime.plusHours(changeableLaneLock.getLockHour());
        }
        if(LocalDateTime.now().isBefore(startTime)){
            this.setStatus(1);
        }else {
            this.setStatus(2);
        }
    }
}
