//package com.supcon.changeablelane.dto;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author JiangWangfa
// * @date 2018/4/22
// * @description 信号机控制方案输出。算法相关的值对象。 指定了对应信号机的相位方案和相相位配时参数。
// */
//@Data
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@Slf4j
//public class AcsOutput {
//
//    private Integer id;
//    private Integer algorithmOutputId;
//
//    @JsonProperty("acsID")
//    private Integer acsId;
//
//    private Integer phaseCount;
//    private List<PhaseOutput> phaseOutputs;
//    private Integer offset;
//
//    /**
//     * 是否启用相位差的标记，为1表示启用，为0表示不启用
//     */
//    private Integer offsetValidFlag;
//
//    /**
//     * 调度模式(默认4)
//     */
//    private Integer scheduleMode;
//    /**
//     * 协调模式
//     */
//    private Integer coordinateMode;
//
//    private String startTime;
//    private Date loadTime;
//    @JsonIgnore
//    private volatile Boolean downFlag;
//
//    public AcsOutput() {
//    }
//
//    public AcsOutput(Integer acsId) {
//        this.acsId = acsId;
//    }
//
//    public Integer getPhaseCount() {
//        if (this.phaseCount == null) {
//            this.phaseCount = this.getPhaseOutputs() == null ? 0 : this.getPhaseOutputs().size();
//        }
//        return this.phaseCount;
//    }
//
//    public List<PhaseOutput> getPhaseOutputs() {
//        if (phaseOutputs != null) {
//            phaseOutputs.forEach(phaseOutput -> phaseOutput.setAcsOutputId(id));
//        }
//        return phaseOutputs;
//    }
//
//    /**
//     * 下发单点时,修改为规定值
//     */
//    public void resetSingle() {
//        this.setScheduleMode(Integer.valueOf(1));
//        this.setStartTime(
//                DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss").format(LocalDateTime.now()));
//    }
//}
