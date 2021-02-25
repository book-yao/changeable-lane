package com.supcon.changeablelane.client.dto;

import lombok.Data;

/**
 * 车道方案
 *
 * @author JWF
 * @date 2020/3/10
 */
@Data
public class LaneScheme {
  private Integer id;

  private Integer laneId;
  /** 备选方案的可变车道类型，1表示进口车道，2表示出口车道 */
  private Integer laneType = 1;
  /** 备选方案的可变车道流向类型，1表示左转，2表示直行，3表示右转，4表示红叉（禁行），5表示直左，6表示直右，7表示左直右（通行） */
  private Integer status;
  /** 延迟时间，单位是秒 */
  private Integer delayTime;
}
