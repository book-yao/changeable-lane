package com.supcon.changeablelane.domain.scheme;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.supcon.changeablelane.constant.AlgorithmModeEnum;
import com.supcon.changeablelane.utils.UUIDUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author JiangWangfa
 * @date 2018/4/22
 * @description 信号机控制方案输出。算法相关的值对象。 指定了对应信号机的相位方案和相相位配时参数。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Slf4j
public class AcsOutput {

    private Integer id;
    private Integer acsID;
    private Integer phaseCount;
    private List<PhaseOutput> phaseOutputs;
    private Integer offset;

    private Integer outputId;
    /**
     * 是否启用相位差 单点自适应路口：0;其他：1
     */
    private Integer offsetValidFlag = 1;

    private String acsName;
    /**
     * 调度模式(默认4)，1：单点
     */
    private Integer scheduleMode;
    /**
     * 协调模式（1：多目标协调/算法，0：单点固定配时）
     */
    private Integer coordinateMode;

    private String startTime;
    @JsonIgnore
    private String uuid = UUIDUtil.getUUIDBySixTeen();

    /**
     * 专家式知识库中，确定应用的路口标志
     */
    private boolean selected;

    private Integer cycleTime;

    /**
     * 算法模式
     */
    private AlgorithmModeEnum algorithmType;

    private Date loadTime;

    public AcsOutput() {
    }

    public AcsOutput(Integer acsId) {
        this.acsID = acsId;
    }


    public Integer getPhaseCount() {
        if (this.phaseCount == null) {
            this.phaseCount = this.getPhaseOutputs() == null ? 0 : this.getPhaseOutputs().size();
        }
        return this.phaseCount;
    }

    public int getCycleTime() {
        if (cycleTime != null) {
            return cycleTime;
        }
        if (this.getPhaseOutputs() == null) {
            return 0;
        }
        cycleTime = this.getPhaseOutputs().stream().mapToInt(PhaseOutput::getTotalTime).sum();
        return cycleTime;
    }

    public List<PhaseOutput> getPhaseOutputs() {
        if (phaseOutputs != null && id != null) {
            phaseOutputs.stream().forEach(phaseOutput -> phaseOutput.setAcsOutputId(id));
        }
        return phaseOutputs;
    }

    public void setPhaseOutputs(List<PhaseOutput> phaseOutputs) {
        if (phaseOutputs == null) {
            return;
        }

        phaseOutputs.sort(Comparator.comparingInt(PhaseOutput::getIndex));

        for (int i = 0; i < phaseOutputs.size(); i++) {
            phaseOutputs.get(i).setIndex(i + 1);
        }
        this.phaseOutputs = phaseOutputs;
    }

    /**
     * 处理双周期流向问题
     */
    public void handleDoubleCycleMovements() {
        Integer phaseCount = this.getPhaseCount();
        if (Objects.equals(phaseCount, 0)) {
            return;
        }
        int n = phaseCount / 2;
        List<PhaseOutput> phaseOutputs = this.getPhaseOutputs();
        for (int i = n; i < phaseCount; i++) {
            List<MovementScheme> movementSchemes = phaseOutputs.get(i - n).getMovementSchemes();
            List<MovementScheme> newMovements = new ArrayList<>();
            if (movementSchemes != null) {
                movementSchemes.forEach(
                        movementScheme -> {
                            MovementScheme movement = new MovementScheme();
                            BeanUtils.copyProperties(movementScheme, movement);
                            newMovements.add(movement);
                        });
                phaseOutputs.get(i).setMovementSchemes(newMovements);
            }
        }
    }

    public void setAcsID(Integer acsID) {
        this.acsID = acsID;
    }

    @JsonSetter(value = "acsId")
    public void setAcsId(Integer acsId) {
        this.acsID = acsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof AcsOutput)) {
            return false;
        }

        AcsOutput acsoutput = (AcsOutput) o;

        return acsID.equals(acsoutput.acsID);
    }

    @Override
    public int hashCode() {
        return acsID;
    }

    public Integer obtainCycleTime() {
        if (phaseOutputs == null) {
            return null;
        }
        return phaseOutputs.stream().mapToInt(phaseOutput -> phaseOutput.getTotalTime()).sum();
    }

    public Integer obtainAllGreenTime() {
        if (phaseOutputs == null) {
            return null;
        }
        return phaseOutputs.stream().mapToInt(phaseOutput -> phaseOutput.getGreenTime()).sum();
    }

    public Integer obtainAllRedTime() {
        if (phaseOutputs == null) {
            return null;
        }
        return phaseOutputs.stream().mapToInt(phaseOutput -> phaseOutput.getRedTime()).sum();
    }

    public Integer obtainAllYellowTime() {
        if (phaseOutputs == null) {
            return null;
        }
        return phaseOutputs.stream().mapToInt(phaseOutput -> phaseOutput.getYellowTime()).sum();
    }

    public List<Integer> obtainAllMovements() {
        if (phaseOutputs == null) {
            return null;
        }
        return phaseOutputs.stream()
                .flatMapToInt(
                        phaseOutput -> {
                            List<MovementScheme> movementSchemes = phaseOutput.getMovements();
                            if (movementSchemes == null) {
                                return IntStream.empty();
                            }
                            return movementSchemes.stream().mapToInt(MovementScheme::getMovementID);
                        })
                .boxed()
                .collect(Collectors.toList());
    }

    public String obtainSplitMovements() {
        List<PhaseOutput> phaseOutputs = this.getPhaseOutputs();
        if (phaseOutputs == null || phaseOutputs.isEmpty()) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        phaseOutputs.stream()
                .forEachOrdered(
                        phaseOutput -> {
                            List<MovementScheme> movementSchemes = phaseOutput.getMovements();
                            if (movementSchemes == null || movementSchemes.isEmpty()) {
                                return;
                            }
                            StringBuffer stringBuffer = new StringBuffer();
                            movementSchemes.stream()
                                    .mapToInt(MovementScheme::getMovementID)
                                    .forEach(i -> stringBuffer.append(i).append(","));
                            if (stringBuffer.length() > 0) {
                                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                            }

                            buffer.append(stringBuffer).append("|");
                        });

        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }

        return buffer.toString();
    }

    public String obtainPhaseTime() {
        if (this.getPhaseOutputs() == null || this.getPhaseOutputs().isEmpty()) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        this.getPhaseOutputs().stream()
                .forEachOrdered(phaseOutput -> buffer.append(phaseOutput.obtainPhaseTime()).append("|"));
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    /**
     * 下发单点时,修改为规定值
     */
    public void resetSingle() {
        this.setScheduleMode(1);
        this.setCoordinateMode(0);
        this.setOffsetValidFlag(0);
        this.setStartTime(
                DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss").format(LocalDateTime.now()));
        this.algorithmType = AlgorithmModeEnum.SINGLE;
    }

    public void runSingleAdaptive() {
        this.setScheduleMode(4);
        this.setCoordinateMode(1);
        this.setOffsetValidFlag(0);
        if (startTime == null) {
            this.setStartTime(
                    DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss").format(LocalDateTime.now()));
        }
        this.setAlgorithmType(AlgorithmModeEnum.SINGLE_ADAPTIVE);
    }

    public void runSplitAlgorithm() {
        this.setScheduleMode(4);
        this.setCoordinateMode(1);
        this.setOffsetValidFlag(0);
        if (startTime == null) {
            this.setStartTime(
                    DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss").format(LocalDateTime.now()));
        }
        this.setAlgorithmType(AlgorithmModeEnum.SPLIT_SCHEME);
    }

    public void runTrusteeship() {
        this.setScheduleMode(4);
        this.setCoordinateMode(1);
        this.setOffsetValidFlag(0);
        if (startTime == null) {
            this.setStartTime(
                    DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss").format(LocalDateTime.now()));
        }
        this.setAlgorithmType(AlgorithmModeEnum.AI_TRUSTEESHIP_SCHEME);
    }

    public LocalTime obtainStartTime() {
        try {
            String startTime = this.getStartTime();
            return LocalTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss"));
        } catch (Exception e) {
            log.error("转换startTime失败，cause:{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public LocalDateTime localStartTime() {
        try {
            String startTime = this.getStartTime();
            if (startTime == null) {
                return null;
            }
            return LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss"));
        } catch (Exception e) {
            log.error("转换startTime失败，cause:{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public LocalTime obtainEndTime(LocalDateTime localDateTime) {
        try {
            if (localDateTime == null) {
                localDateTime = this.localStartTime();
            }

            if (localDateTime == null) {
                return null;
            }
            LocalDateTime localDateTime1 = localDateTime.plusSeconds(this.getCycleTime());
            if ((localDateTime1.toLocalDate().toEpochDay() - localDateTime.toLocalDate().toEpochDay())
                    > 0) {
                return LocalTime.of(23, 59, 59);
            }
            return localDateTime1.toLocalTime();
        } catch (Exception e) {
            log.error("转换endTime失败，cause:{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    @JsonSetter("acsSplitOutput")
    public void setAcsSplitOutput(List<PhaseOutput> phaseOutputs) {
        this.phaseOutputs = phaseOutputs;
    }


}
