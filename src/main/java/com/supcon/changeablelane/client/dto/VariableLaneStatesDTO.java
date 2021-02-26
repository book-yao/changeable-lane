package com.supcon.changeablelane.client.dto;

import com.supcon.changeablelane.domain.VariableLaneDTO;
import com.supcon.changeablelane.dto.VariableLaneControl;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class VariableLaneStatesDTO {
  private List<VariableLaneStateDTO> laneStates;

  public VariableLaneControl convertToVariableLaneControlByNoChangeLane(Integer acsId) {
    if (CollectionUtils.isEmpty(laneStates)) {
      return null;
    }

    List<VariableLaneDTO> variableLaneDTOS =
        laneStates.stream()
            .map(
                variableLaneStateDTO ->
                    variableLaneStateDTO.convertToVariableLaneDTO(2))
            .collect(Collectors.toList());
    return VariableLaneControl.builder().acsId(acsId).variableLaneList(variableLaneDTOS).build();
  }
}
