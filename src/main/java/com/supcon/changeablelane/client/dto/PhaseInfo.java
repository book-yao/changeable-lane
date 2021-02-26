package com.supcon.changeablelane.client.dto;

import com.supcon.changeablelane.domain.scheme.MovementScheme;
import com.supcon.changeablelane.domain.scheme.PhaseOutput;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiangWangfa
 * @date 2018/4/22
 * @description 信号机当前正在运行的控制方案中的相位信息。
 * 算法相关的值对象，包含相位时间和所包含的组合流向。
 */
@Data
public class PhaseInfo {
    private Integer id;
    private int acsId;
    private int phaseId;
    private int phaseTime;
    private List<Integer> movementNumbers;


    public PhaseOutput phaseInfo2PhaseOutput(Integer acsId){
        PhaseOutput phaseOutput = new PhaseOutput();
        phaseOutput.setIndex(this.getPhaseId());
        phaseOutput.setSplitValue(null);
        phaseOutput.setGreenTime(this.getPhaseTime()-5);
        phaseOutput.setYellowTime(2);
        phaseOutput.setRedTime(3);
        phaseOutput.setTotalTime(this.getPhaseTime());
        phaseOutput.setMovements(obtainMovements(this.getMovementNumbers()));
        return phaseOutput;
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
