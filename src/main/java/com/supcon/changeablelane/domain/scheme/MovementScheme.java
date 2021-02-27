package com.supcon.changeablelane.domain.scheme;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * @author JiangWangfa
 * @date 2018/4/22
 * @description 组合流向方案。算法相关的值对象。 指定了组合流向号、是否放行、是否迟起等，用于算法过程。
 */
@Data
public class MovementScheme {
  private Long id;
  private Integer movementID;
  private boolean release;
  private boolean delay;
  private Integer delayedTime;
  private boolean turnOff;
  private boolean yellowFlash;
  /** 是否可变车道 add by JWF 2019/8/19 */
  private Boolean isVarLane;
  /** 可变车道方向 add by JWF 2019/8/19 */
  private Integer varLaneMovementType;
  /** 可变车道所属的进口ID add by JWF 2019/8/19 */
  private Integer entranceId;
  /** 车道id,用于给可变车道算法 add by JWF 2019/8/19 */
  private Integer laneId;

  /**
   * 过滤可变车道信号灯红灯的情况
   *
   * @return
   */
  public boolean filterRedLightState() {
    if (!release && !delay && !turnOff && !yellowFlash) {
      return false;
    }
    return true;
  }

  @JsonSetter("movementId")
  public void setMovement(Integer movementId) {
    this.movementID = movementId;
  }

  @JsonGetter("movementId")
  public Integer getMovementId() {
    return this.movementID;
  }
}
