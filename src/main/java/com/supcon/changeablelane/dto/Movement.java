package com.supcon.changeablelane.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supcon.changeablelane.constant.DirectionTypeEnum;
import com.supcon.changeablelane.constant.MovementTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JWF
 * @date 2020-08-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movement {
  /** 流向ID（流向号） */
  @JsonProperty("number")
  private Integer movementId;

  /** 流向类型 */
  private MovementTypeEnum movementType;

  /** 进口号 */
  private DirectionTypeEnum entranceDirection;
}
