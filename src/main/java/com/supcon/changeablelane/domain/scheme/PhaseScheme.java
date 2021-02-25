package com.supcon.changeablelane.domain.scheme;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidewen
 * @date 2018/8/27
 * @description 相位方案
 */
@Data
public class PhaseScheme {
    /**
     * 方案号
     */
    private Integer id;

    /**
     * 信号周期时间
     */
    private Integer cycleTime;

    /**
     * 相位差
     */
    private Integer offset;

    /**
     * 可变车道
     */
    private Integer changeableLane;

    /**
     * 相位差基准相位
     */
    private Integer basePhaseId;

    /**
     * 相位配时方案
     */
    private List<com.supcon.changeablelane.domain.PhaseScheme> phaseTimings;

    public AcsOutput dto2AcsOutput() {
        AcsOutput acsOutput = new AcsOutput();
        acsOutput.setOffset(offset);
        if (this.phaseTimings != null) {
            List<PhaseOutput> collect = this.phaseTimings.stream().map(com.supcon.changeablelane.domain.PhaseScheme::phaseScheme2PhaseOutput).collect(Collectors.toList());
            acsOutput.setPhaseOutputs(collect);
            acsOutput.setPhaseCount(collect.size());
        }
        return acsOutput;
    }
}
