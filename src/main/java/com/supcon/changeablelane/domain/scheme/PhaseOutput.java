package com.supcon.changeablelane.domain.scheme;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiangWangfa
 * @date 20185/4/22
 * @description 相位控制方案输出。算法相关的值对象。 指定了某一信号机中各个相位的组合流向、绿灯时间等配时参数。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PhaseOutput {
  private int index;
  private List<MovementScheme> movements;
  private int greenTime;
  private int yellowTime;
  private int redTime;

  @JsonProperty(value = "key")
  private int id;

  @JsonIgnore
  private String loadTime;
  private Integer acsOutputId;
//  @JsonIgnore
  private int totalTime;

  /**
   * 通行流量
   *
   * @add by JWF 2019/5/27
   */
  private Integer trafficFlow;
  /**
   * 绿信比方案的比值
   *
   * @add by JWF 2020/5/27
   */
  private Integer splitValue;

  @JsonGetter("movements")
  public List<MovementScheme> getMovements() {
    return movements;
  }

  @JsonSetter("movements")
  public void setMovements(List<MovementScheme> movements) {
    this.movements = movements;
  }


  public List<MovementScheme> getMovementSchemes() {
    return movements;
  }


  public void setMovementSchemes(List<MovementScheme> movements) {
    this.movements = movements;
  }

  /** 设置总的时间 */
  public void setTotalTime() {
    this.totalTime = greenTime + yellowTime + redTime;
  }

  public int getTotalTime() {
    return greenTime + yellowTime + redTime;
  }

  public String obtainPhaseTime() {
    return greenTime + "," + yellowTime + "," + redTime;
  }

  @JsonSetter("phaseId")
  public void setPhaseId(Integer phaseId) {
    this.index = phaseId;
  }

  /**
   * 构建movementScheme列表
   *
   * @param movementNumbers
   * @return
   */
  private List<MovementScheme> obtainMovements(List<Integer> movementNumbers) {
    //如果流向为空，则填充一个不放行的流向
    if (CollectionUtils.isEmpty(movementNumbers)) {

      return new ArrayList<>();
    }
    return movementNumbers.stream()
            .map(item -> {
              MovementScheme movementScheme = new MovementScheme();
              movementScheme.setMovementID(item);
              movementScheme.setRelease(true);
              movementScheme.setDelay(false);
              movementScheme.setYellowFlash(false);
              movementScheme.setTurnOff(false);
              return movementScheme;
            })
            .collect(Collectors.toList());
  }
}
