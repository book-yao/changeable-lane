package com.supcon.changeablelane.dto;

import com.supcon.changeablelane.domain.VariableLaneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariableLaneControl {
  private Integer acsId;
  private List<VariableLaneDTO> variableLaneList;

  public VariableLaneControl(Integer acsId, List<VariableLaneDTO> variableLaneDTOS, Integer lockTime) {
      if(Objects.nonNull(lockTime)){
        variableLaneDTOS.stream()
                .forEach(item->{
                  item.setLockTime(lockTime);
                });
      }
    this.acsId = acsId;
    this.variableLaneList=variableLaneDTOS;
  }
}
