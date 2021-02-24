package com.supcon.changeablelane.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author caowenbo
 * @create 2021/2/24 10:07
 */
@Data
public class ChangeableLaneLock {
    private int id;

    private int areaId;

    private LocalDateTime startTime;

    private int lockHour;

    private int lockMinute;

    //固定值 早，中，晚 高峰
    private int schemeId;

    public boolean isValid() {
        if(Objects.isNull(startTime)){
            return false;
        }
        LocalDateTime startTime = this.getStartTime();
        if(Objects.nonNull(this.getLockHour())){
            startTime = startTime.plusHours(this.getLockHour());
        }
        if(Objects.nonNull(this.getLockMinute())){
            startTime = startTime.plusHours(this.getLockHour());
        }
        if(LocalDateTime.now().isBefore(startTime)){
           return true;
        }else {
            return false;
        }
    }
}
