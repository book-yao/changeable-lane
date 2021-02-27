package com.supcon.changeablelane.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariableLaneDTO {

  private int id;

  private int schemeId;

  private int intersectionId;

  private int acsId;
  /** 进口id */
  private Integer entranceId;
  /** 控制模式：1-手动 2-算法 3-定时 4-解锁 */
  private int mode;
  /** 周期开始后经过多少秒开始改变状态 */
  private Integer delayTime;
  /** 锁定时长 */
  private Integer lockTime;
  /** 车道id */
  private Integer laneId;
  /** 可变车道当前车道状态: 1-左 2-直 3-右 */
  private Integer state;

  private Integer type;
}
