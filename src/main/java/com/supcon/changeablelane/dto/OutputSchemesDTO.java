package com.supcon.changeablelane.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
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

@Data
public class OutputSchemesDTO {
    private List<Output> schemeOutputs;

    public OutputSchemesDTO() {
    }

    public OutputSchemesDTO(AcsOutput acsOutput, Integer transitionalRunTime) {
        // 不启用相位差
        acsOutput.setOffsetValidFlag(0);
        acsOutput.setCoordinateMode(1);
        acsOutput.setScheduleMode(4);
        acsOutput.setLoadTime(new Date());
        Output output = new Output();
        output.setAcsOutputs(Arrays.asList(acsOutput));
        output.setKeyAcsID(acsOutput.getAcsID());
        output.setStatusCode(2);
        output.setSubAreaID(acsOutput.getAcsID());
        output.setLockTime(transitionalRunTime);
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
