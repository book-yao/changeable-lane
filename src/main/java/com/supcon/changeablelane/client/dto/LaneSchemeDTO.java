package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * 车道方案
 *
 * @author JWF
 * @date 2020/3/18
 */
@Data
public class LaneSchemeDTO extends LaneScheme {
  private Integer entranceId;

  @JsonSetter("dir")
  public void setDir(Integer dir) {
    this.setStatus(dir);
  }
}
