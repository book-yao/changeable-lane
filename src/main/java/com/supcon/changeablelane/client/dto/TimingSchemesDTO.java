package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JWF
 * @date 2020/3/18
 */
@Data
public class TimingSchemesDTO {
  @JsonProperty("acsSchemes")
  public List<TimingSchemeDTO> timingSchemes;
}
