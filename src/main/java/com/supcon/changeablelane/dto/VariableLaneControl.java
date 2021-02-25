package com.supcon.changeablelane.dto;

import com.supcon.changeablelane.domain.VariableLaneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariableLaneControl {
  private Integer acsId;
  private List<VariableLaneDTO> variableLaneList;
}
