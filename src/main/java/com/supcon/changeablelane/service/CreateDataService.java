package com.supcon.changeablelane.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.supcon.changeablelane.client.VariableLaneClient;
import com.supcon.changeablelane.client.dto.TimingSchemeDTO;
import com.supcon.changeablelane.domain.VariableDriveway;
import com.supcon.changeablelane.domain.VariableLaneDTO;
import com.supcon.changeablelane.mapper.SchemeMapper;
import com.supcon.changeablelane.mapper.VariableLaneSchemeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Value("#{${param.acsId-intersectionId}}")
    private Map<Integer,Integer> acsIdIntersectionIdMap;


    public void createTimingScheme(Integer acsId, Integer index, Integer schemeId,Integer type) {
        List<VariableLaneDTO> variableLaneDTOS = new ArrayList<>();
        Integer intersectionId = acsIdIntersectionIdMap.get(acsId);
        if(java.util.Objects.isNull(intersectionId)){
            return;
        }
        variableLaneClient.getTimingSchemeByAcsId(intersectionId)
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
                                            variableLaneDTO.setType(type);
                                            variableLaneDTO.setAcsId(acsId);
                                            if(java.util.Objects.nonNull(acsIdIntersectionIdMap.get(acsId))){
                                                variableLaneDTO.setIntersectionId(acsIdIntersectionIdMap.get(acsId));
                                            }
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


    public void insertIntoVariableLaneCard(List<JSONObject> list) {
        if(!CollectionUtils.isEmpty(list)){
            variableLaneSchemeMapper.insertVariableLane(list);
        }
    }
}
