package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 定时方案
 *
 * @author JWF
 * @date 2020/3/18
 */
@Data
public class TimingSchemeDTO {
  private Integer acsId;
  private Integer schemeId;
  private String name;

  @JsonProperty("schemeList")
  private List<LaneSchemeDTO> laneSchemes;

  public List<LaneSchemeDTO> getLaneSchemes() {
    return laneSchemes == null ? Collections.EMPTY_LIST : laneSchemes;
  }
}
