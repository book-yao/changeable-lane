package com.supcon.changeablelane.dto;

import com.supcon.changeablelane.domain.ChangeableLaneLock;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author caowenbo
 * @create 2021/2/24 10:07
 */
@Data
public class ChangeableLaneLockDTO {
    private Integer id;

    private Integer areaId;

    private LocalDateTime startTime;

    private Integer lockHour;

    private Integer lockMinute;

    //固定值 早，中，晚 高峰
    private Integer schemeId;

    /**
     * 1.锁定；2解锁
     */
    private Integer lockType;

    public ChangeableLaneLock dto2ChangeableLaneLock(Integer areaId) {
        ChangeableLaneLock changeableLaneLock = new ChangeableLaneLock();
        changeableLaneLock.setAreaId(areaId);
        if(Objects.nonNull(this.getLockHour())){
            changeableLaneLock.setLockHour(this.getLockHour());
        }
        if(Objects.nonNull(this.getLockMinute())){
            changeableLaneLock.setLockMinute(this.getLockMinute());
        }
        changeableLaneLock.setSchemeId(this.getSchemeId());
        changeableLaneLock.setStartTime(this.getStartTime());
        return changeableLaneLock;
    }

    /**
     * 获取锁定时间数
     * @return
     */
    public Integer getLockTime() {
        int lockTime = 0;
        if(Objects.nonNull(this.getLockHour())){
            lockTime = this.getLockHour()*60+lockTime;
        }
        if(Objects.nonNull(this.getLockMinute())){
            lockTime = lockTime+this.getLockMinute();
        }
        return lockTime;
    }
}
