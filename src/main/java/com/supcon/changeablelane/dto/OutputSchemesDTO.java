package com.supcon.changeablelane.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.supcon.changeablelane.constant.AlgorithmModeEnum;
import com.supcon.changeablelane.constant.SchemeSourceEnum;
import com.supcon.changeablelane.constant.SchemeTypeEnum;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class OutputSchemesDTO {
    private List<Output> schemeOutputs;

    public OutputSchemesDTO() {
    }

    public OutputSchemesDTO(Integer acsId,AcsOutput acsOutput, Integer lockTime,Integer transitionalRunTime) {
        if(Objects.isNull(acsOutput)){
            acsOutput = new AcsOutput();
            acsOutput.setAcsId(acsId);
            acsOutput.resetSingle();
        }else if(!Objects.equals(acsOutput.getAlgorithmType(),AlgorithmModeEnum.SINGLE)){
            acsOutput.setOffsetValidFlag(0);
            acsOutput.setCoordinateMode(1);
            acsOutput.setScheduleMode(4);
            acsOutput.setAlgorithmType(AlgorithmModeEnum.SINGLE_ADAPTIVE);
        }
        // 不启用相位差
        acsOutput.setAcsID(acsId);
        acsOutput.setStartTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss")));
        acsOutput.setLoadTime(new Date());
        Output output = new Output();
        output.setAcsOutputs(Arrays.asList(acsOutput));
        output.setKeyAcsID(acsOutput.getAcsId());
        output.setStatusCode(2);
        output.setSubAreaID(acsOutput.getAcsId());
        output.setLockTime(lockTime);
        output.setSchemeSource(SchemeSourceEnum.SUPCONIT);
        output.setLoadTime(
                DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm,ss").format(LocalDateTime.now()));
        output.setSchemeType(SchemeTypeEnum.CHANGEABLE_LANE);
        this.schemeOutputs = Arrays.asList(output);
    }

    public Integer obtainAcsId() {
        if (CollectionUtils.isEmpty(schemeOutputs)) {
            return null;
        }
        return schemeOutputs.get(0).getKeyAcsID();
    }

    @JsonGetter("output")
    public List<Output> getSchemeOutputs() {
        return schemeOutputs;
    }
}
