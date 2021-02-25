package com.supcon.changeablelane.domain;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import lombok.Data;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 19:43
 */
@Data
public class ChangeableLaneScheme {
    private Integer acsId;

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

}
