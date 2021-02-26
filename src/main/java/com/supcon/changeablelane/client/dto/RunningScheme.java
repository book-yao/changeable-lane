package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import com.supcon.changeablelane.domain.scheme.PhaseOutput;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author JiangWangfa
 * @date 2018/4/22
 * @description 信号机当前正在运行的控制方案。 算法相关的值对象，用于算法过程。
 */
@Data
public class RunningScheme {
  private int acsId;
  private int phaseOffset;
  private List<PhaseInfo> phaseInfos;
  private String startTime;
  private Integer cycleTime;
  /**
   * 控制方式
   */
  private int controlWay;

  public LocalDateTime getStartTimeToDate() {
    String epochTime = startTime;
    int lastPointIndex;
    String point = ".";
    if ((lastPointIndex = startTime.lastIndexOf(point)) > -1) {
      epochTime = startTime.substring(0, lastPointIndex);
    }
    return LocalDateTime.parse(epochTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  /**
   * 获取epoch时间(秒级)
   *
   * @return
   */
  @JsonGetter("epochTime")
  public String getEpochTime() {
    if (startTime == null) {
      return null;
    }
    int lastPointIndex;
    String epochTime = startTime;
    String point = ".";
    if ((lastPointIndex = startTime.lastIndexOf(point)) > -1) {
      epochTime = startTime.substring(0, lastPointIndex);
    }

    try {
      long l =
              LocalDateTime.parse(epochTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                      .toInstant(ZoneOffset.of("+8"))
                      .toEpochMilli();
      return String.valueOf(l);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public AcsOutput runningScheme2AcsOutput(){
    AcsOutput acsOutput = new AcsOutput();
    acsOutput.setAcsId(this.getAcsId());
    acsOutput.setOffset(this.getPhaseOffset());
    acsOutput.setCycleTime(this.getCycleTime());
    acsOutput.setStartTime(this.getStartTime());
    acsOutput.setPhaseOutputs(phaseInfos.stream()
            .map(item -> {
              return item.phaseInfo2PhaseOutput(this.getAcsId());
            }).collect(Collectors.toList()));
    return acsOutput;
  }

}
