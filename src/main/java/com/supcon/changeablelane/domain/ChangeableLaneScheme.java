package com.supcon.changeablelane.domain;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.domain.scheme.PhaseOutput;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author caowenbo
 * @create 2021/2/24 19:43
 */
@Data
public class ChangeableLaneScheme {
    private Integer acsId;

    private Integer intersectionId;

    private String acsName;

    private List<VariableLaneDTO> variableLaneSchemes;

    /**
     * 诱导屏方案信息
     */
    private List<TrafficScreenScheme> trafficScreenSchemes;

    private AcsOutput acsOutputs;

    public void setAcsSchemeInfo(AcsSchemeInfo acsSchemeInfo) {
        JSONObject jsonObject = JSONObject.parseObject(acsSchemeInfo.getAcsOutputText());
        PhaseScheme phaseScheme = jsonObject.toJavaObject(PhaseScheme.class);
        this.setAcsOutputs(phaseScheme.dto2AcsOutput());
    }

    public List<VariableLaneDTO> getVariableLaneSchemesByType(Integer type) {
        return variableLaneSchemes.stream()
                .filter(item-> Objects.equals(item.getType(),type))
                .collect(Collectors.toList());
    }

    public List<VariableLaneDTO> geTemporaryVariableLaneSchemes() {
        return variableLaneSchemes.stream()
                .filter(item-> !Objects.equals(item.getType(),1))
                .collect(Collectors.toList());
    }


    public void compareOtherChangeableLaneScheme(ChangeableLaneScheme otherScheme) {
        try {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> this.compareAcsOutput(this.acsOutputs, otherScheme.getAcsOutputs())),
                    CompletableFuture.runAsync(() -> this.compareTrafficScreenScheme(this.trafficScreenSchemes, otherScheme.getTrafficScreenSchemes())),
                    CompletableFuture.runAsync(() -> this.compareVariableLaneSchemes(this.geTemporaryVariableLaneSchemes(), otherScheme.geTemporaryVariableLaneSchemes()))
            ).get();
        } catch (Exception e) {
        }
    }

    /**
     * 对比可变车道方案
     * @param variableLaneSchemesByType
     * @param
     */
    private void compareVariableLaneSchemes(List<VariableLaneDTO> variableLaneSchemesByType, List<VariableLaneDTO> otherVariableLaneSchemesByType) {
        variableLaneSchemesByType.stream()
                .forEach(item->{
                    VariableLaneDTO variableLaneDTO1 = otherVariableLaneSchemesByType.stream()
                            .filter(variableLaneDTO -> Objects.equals(item.getEntranceId(), variableLaneDTO.getEntranceId())
                                    && Objects.equals(item.getLaneId(), variableLaneDTO.getLaneId()))
                            .findFirst()
                            .orElse(null);
                    if(Objects.isNull(variableLaneDTO1)){
                        item.setIsChange(1);
                    }else {
                        if(Objects.equals(item.getState(),variableLaneDTO1.getState())){
                            item.setIsChange(0);
                        }else {
                            item.setIsChange(1);
                        }
                    }
                });
    }

    /**
     * 对比诱导屏方案
     * @param trafficScreenSchemes
     * @param
     */
    private void compareTrafficScreenScheme(List<TrafficScreenScheme> trafficScreenSchemes, List<TrafficScreenScheme> otherTrafficScreenSchemes) {
        trafficScreenSchemes.stream()
                .forEach(item->{
                    TrafficScreenScheme trafficScreenScheme1 = otherTrafficScreenSchemes.stream()
                            .filter(trafficScreenScheme -> Objects.equals(trafficScreenScheme.getDeviceCode(), item.getDeviceCode()))
                            .findFirst()
                            .orElse(null);
                    if(Objects.isNull(trafficScreenScheme1)){
                        item.setIsChange(1);
                    }else {
                        if(Objects.equals(item.getMessageOne(),trafficScreenScheme1.getMessageOne())
                                &&Objects.equals(item.getMessageTwo(),trafficScreenScheme1.getMessageTwo())){
                            item.setIsChange(0);
                        }else {
                            item.setIsChange(1);
                        }
                    }
                });
    }

    /**
     * 对比信号机方案
     * @param acsOutputs
     * @param otherAcsOutputs
     */
    private void compareAcsOutput(AcsOutput acsOutputs, AcsOutput otherAcsOutputs) {
        acsOutputs.getPhaseOutputs().stream()
                .forEach(item->{
                    PhaseOutput phaseOutput1 = otherAcsOutputs.getPhaseOutputs().stream()
                            .filter(phaseOutput -> Objects.equals(phaseOutput.getIndex(), item.getIndex()))
                            .findFirst()
                            .orElse(null);
                    if(Objects.isNull(phaseOutput1)){
                        item.setIsChange(1);
                    }else{
                        if(Objects.equals(phaseOutput1.getTotalTime(),item.getTotalTime())){
                            item.setIsChange(0);
                        }else {
                            item.setIsChange(0);
                        }
                    }

                });
    }
}
