package com.supcon.changeablelane.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.supcon.changeablelane.domain.scheme.MovementScheme;
import lombok.Data;

import java.util.List;

/**
 * @author JiangWangfa
 * @date 20185/4/22
 * @description 相位控制方案输出。算法相关的值对象。 指定了某一信号机中各个相位的组合流向、绿灯时间等配时参数。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PhaseOutput {
  private Integer id;
  private Integer acsOutputId;
  private Integer index;
  private List<MovementScheme> movements;
  private Integer greenTime;
  private Integer yellowTime;
  private Integer redTime;

  /** 设置总的时间 */
  public String obtainPhaseTime() {
    return greenTime + "," + yellowTime + "," + redTime;
  }

  public Integer ObtainPhaseTime() {
    return greenTime + yellowTime + redTime;
  }
}
