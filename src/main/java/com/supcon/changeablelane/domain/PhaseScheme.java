package com.supcon.changeablelane.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supcon.changeablelane.domain.scheme.MovementScheme;
import com.supcon.changeablelane.domain.scheme.PhaseOutput;
import com.supcon.changeablelane.utils.NumberUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author JiangWangfa
 * @date 2018/4/22
 * @description 相位方案。算法相关的值对象。 指定了绿灯时间、红灯时间等相位配时参数等，用于信号机的预设相位配置。
 */
@Data
// @JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(
        ignoreUnknown = true,
        value = {"hibernateLazyInitializer", "handler"})
public class PhaseScheme implements Serializable {
    /**
     * 表id add by JWF 2018/8/14
     */
    private Integer id;

    private Integer number;
    private Integer greenTime;
    private Integer yellowTime;
    private Integer redTime;
    private Integer minGreenTime;
    private Integer maxGreenTime;
    private float saturationCorrectionFactor = 1;
    /**
     * 是否协调相位
     */
    private boolean coordinatePhase;

    private List<MovementScheme> movementSchemes;
    /**
     * 倾斜系数，与报警等级相关
     */
    private List<Float> inclinationCoefficients;
    /**
     * 主协调相位 add by JWF 2020-09-02
     */
    private Boolean mainCoordinatePhase;

    public void filterNonRelease() {
        if (this.movementSchemes == null) {
            return;
        }

        this.movementSchemes =
                this.movementSchemes.stream()
                        .filter(
                                movementScheme ->
                                        movementScheme.isRelease() || Objects.equals(movementScheme.getIsVarLane(), Boolean.TRUE))
                        .collect(Collectors.toList());
    }

    /**
     * 过滤关灯或者黄闪的的数据
     *
     * @return 关灯或者黄闪的的数据
     */
    public List<MovementScheme> filterTurnOffOrYellowFlashMovementSchemes() {
        if (this.getMovementSchemes() == null) {
            return new ArrayList<>();
        }

        int maxMovementNum = 16;
        List<MovementScheme> res =
                this.getMovementSchemes().stream()
                        .filter(
                                movementScheme ->
                                        movementScheme.getMovementID() > maxMovementNum
                                                || movementScheme.isTurnOff()
                                                || movementScheme.isYellowFlash()
                                                //                        || movementScheme.isDelay()
                                                || !movementScheme.isRelease())
                        .collect(Collectors.toList());

        this.movementSchemes =
                this.getMovementSchemes().stream()
                        .filter(
                                movementScheme ->
                                        !(movementScheme.getMovementID() > maxMovementNum
                                                || movementScheme.isTurnOff()
                                                || movementScheme.isYellowFlash()
                                                //                        || movementScheme.isDelay()
                                                || !movementScheme.isRelease()))
                        .collect(Collectors.toList());
        return res;
    }

    /**
     * @description: 获取红绿黄总时间
     * @param: []
     * @return: int
     * @author: JiangWangfa
     * @date: 2018/10/26 14:02
     */
    public int getTotalTime() {
        return this.getYellowTime() + this.getRedTime() + this.getGreenTime();
    }

    /**
     * @description: 获取最小绿+红+黄 时间
     * @param: []
     * @return: int
     * @author: JiangWangfa
     * @date: 2018/10/26 14:52
     */
    public int getMinGreenAndRedYellow() {
        return this.minGreenTime + this.redTime + this.yellowTime;
    }

    /**
     * @description: 获取最大绿+红+黄 时间
     * @param: []
     * @return: int
     * @author: JiangWangfa
     * @date: 2018/10/26 14:53
     */
    public int getMaxGreenAndRedYellow() {
        return this.maxGreenTime + this.redTime + this.yellowTime;
    }

    public PhaseOutput phaseScheme2PhaseOutput() {
        PhaseOutput phaseOutput = new PhaseOutput();
        phaseOutput.setIndex(this.number);
        phaseOutput.setId(NumberUtil.getRandomNumber());
        phaseOutput.setYellowTime(this.yellowTime);
        phaseOutput.setRedTime(this.redTime);
        phaseOutput.setGreenTime(this.getGreenTime());
        phaseOutput.setMovements(this.movementSchemes);
        if (phaseOutput.getMovements() != null) {
            phaseOutput.getMovements().stream()
                    .peek(movementScheme -> movementScheme.setId((long) NumberUtil.getRandomNumber()));
        }
        return phaseOutput;
    }

    public List<MovementScheme> getMovementSchemes() {
        filterNonRelease();
        return movementSchemes == null ? Collections.EMPTY_LIST : movementSchemes;
    }

    public void setMovementSchemes(List<MovementScheme> movementSchemes) {
        if (movementSchemes == null) {
            return;
        }

        this.movementSchemes = movementSchemes;
        // 过滤关起(release==false)的数据
        //        this.movementSchemes = movementSchemes.stream()
        //                .filter(MovementScheme::isRelease).collect(Collectors.toList());
    }

    public Integer getKey() {
        return id;
    }

    public void setKey(Integer id) {
        this.id = id;
    }

    public Boolean getMainCoordinatePhase() {
        return mainCoordinatePhase == null ? Boolean.FALSE : mainCoordinatePhase;
    }
}
