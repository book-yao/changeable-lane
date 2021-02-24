package com.supcon.changeablelane.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author caowenbo
 * @create 2021/2/24 10:07
 */
@Data
public class ChangeableLaneLock {
    private int id;

    private int teamId;

    private LocalDateTime startTime;

    private int lockHour;

    private int lockMinute;

    //固定值 早，中，晚 高峰
    private int schemeId;

}
