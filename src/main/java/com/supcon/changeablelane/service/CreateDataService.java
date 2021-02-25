package com.supcon.changeablelane.service;

import com.google.common.base.Objects;
import com.supcon.changeablelane.client.VariableLaneClient;
import com.supcon.changeablelane.client.dto.TimingSchemeDTO;
import com.supcon.changeablelane.client.dto.TimingSchemesDTO;
import com.supcon.changeablelane.domain.VariableLaneDTO;
import com.supcon.changeablelane.mapper.VariableLaneSchemeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author caowenbo
 * @create 2021/2/25 13:30
 */
@Service
@Slf4j
public class CreateDataService {

    @Autowired
    private VariableLaneClient variableLaneClient;

    @Resource
    private VariableLaneSchemeMapper variableLaneSchemeMapper;


    public void createTimingScheme(Integer acsId, Integer index, Integer schemeId) {
        List<VariableLaneDTO> variableLaneDTOS = new ArrayList<>();
        variableLaneClient.getTimingSchemeByAcsId(acsId)
                .ifPresent(
                timingSchemesDTO -> {
                    List<TimingSchemeDTO> timingSchemes = timingSchemesDTO.getTimingSchemes();
                    if (CollectionUtils.isEmpty(timingSchemes)) {
                        log.info("可变车道 / {} | 获取的定时方案为空", acsId);
                        return;
                    }
                    timingSchemes.stream()
                            .filter(item->Objects.equal(index,item.getSchemeId()))
                            .findFirst()
                            .ifPresent(timingScheme->{
                                timingScheme.getLaneSchemes().stream()
                                        .forEach(laneScheme -> {
                                            VariableLaneDTO variableLaneDTO = new VariableLaneDTO();
                                            variableLaneDTO.setDelayTime(laneScheme.getDelayTime());
                                            variableLaneDTO.setEntranceId(laneScheme.getEntranceId());
                                            variableLaneDTO.setLaneId(laneScheme.getLaneId());
                                            variableLaneDTO.setLockTime(0);
                                            variableLaneDTO.setAcsId(acsId);
                                            variableLaneDTO.setMode(2);
                                            variableLaneDTO.setSchemeId(schemeId);
                                            variableLaneDTO.setState(laneScheme.getStatus());
                                            variableLaneDTOS.add(variableLaneDTO);
                                        });
                                        });
                            });
        if(!CollectionUtils.isEmpty(variableLaneDTOS)){
            variableLaneSchemeMapper.insertVariableLaneScheme(variableLaneDTOS);
        }
    }
}
