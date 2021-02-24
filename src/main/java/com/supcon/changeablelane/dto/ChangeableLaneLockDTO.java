package com.supcon.changeablelane.dto;

import com.supcon.changeablelane.domain.ChangeableLaneLock;
import lombok.Data;

import java.time.LocalDateTime;

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
        changeableLaneLock.setLockHour(this.getLockHour());
        changeableLaneLock.setLockMinute(this.getLockMinute());
        changeableLaneLock.setSchemeId(this.getSchemeId());
        changeableLaneLock.setStartTime(this.getStartTime());
        return changeableLaneLock;
    }
}
