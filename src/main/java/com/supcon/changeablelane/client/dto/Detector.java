package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * @author lidewen
 * @date 2018/8/27
 * @description 检测器相关信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Detector {
  /** 检测器号 */
  private Integer id;

  /** 线圈号 */
  private Integer loopId;

  /** 流向号 */
  private Integer movementId;

  /** 饱和流量 */
  private Integer saturatedFlow;

  /** 是否检测 */
  private Boolean usedForDetect;

  @JsonSetter("movementNumber")
  public void setMovementId(Integer movementId) {
    this.movementId = movementId;
  }
}
