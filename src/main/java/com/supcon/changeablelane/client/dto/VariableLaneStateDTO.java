package com.supcon.changeablelane.client.dto;

import com.supcon.changeablelane.domain.VariableLaneDTO;
import lombok.Data;

@Data
public class VariableLaneStateDTO {
  private Integer acsId;
  private Integer entranceId;
  private Integer laneId;
  /** 可变车道状态：1-在线 2-离线 */
  private Integer state;
  /** 可变车道方向信息：1-左 2-直 3-右 */
  private Integer status;

  public VariableLaneDTO convertToVariableLaneDTO(int mode) {
    return VariableLaneDTO.builder()
        .entranceId(entranceId)
        .laneId(laneId)
        .state(status)
        .mode(mode)
        .build();
  }
}
