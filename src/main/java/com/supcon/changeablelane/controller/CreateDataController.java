package com.supcon.changeablelane.controller;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.DataTrafficClient;
import com.supcon.changeablelane.client.dto.AcsConfigInfoDTO;
import com.supcon.changeablelane.constant.StatusCode;
import com.supcon.changeablelane.domain.ChangeableLaneArea;
import com.supcon.changeablelane.domain.Devices;
import com.supcon.changeablelane.domain.TrafficScreen;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import com.supcon.changeablelane.dto.ChangeableLaneLockDTO;
import com.supcon.changeablelane.dto.ResponseDTO;
import com.supcon.changeablelane.mapper.AcsSchemeMapper;
import com.supcon.changeablelane.service.ChangeableLaneAreaService;
import com.supcon.changeablelane.service.ChangeableLaneLockService;
import com.supcon.changeablelane.service.CreateDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:17
 */

@RequestMapping(value = "/api/v1/changeableLane/")
@RestController
@Api(
        value = "AcsController",
        tags = {"建造演示数据"})
public class CreateDataController {

    @Autowired
    private DataTrafficClient dataTrafficClient;

    @Autowired
    private AcsSchemeMapper acsSchemeMapper;

    @Autowired
    private CreateDataService createDataService;

    @PostMapping(value = "create/acsScheme")
    @ApiOperation(value = "新建信号机方案")
    public ResponseDTO<Void> areas(Integer acsId,Integer index,Integer schemeId){
        Optional<AcsConfigInfoDTO> acsSingleScheme = dataTrafficClient.getAcsSingleScheme(acsId);
        acsSingleScheme.ifPresent(
                acsConfig -> {
                    Optional<PhaseScheme> optional =
                            acsConfig.getPhaseSchemes().stream()
                                    .filter(p -> p.getId().equals(index))
                                    .findFirst();
                    optional.ifPresent(
                            phaseScheme -> {
                                AcsSchemeInfo acsSchemeInfo = new AcsSchemeInfo();
                                acsSchemeInfo.setAcsId(acsId);
                                acsSchemeInfo.setSchemeId(schemeId);
                                acsSchemeInfo.setAcsOutputText(JSONObject.toJSONString(phaseScheme));
                                acsSchemeMapper.insertAcsScheme(acsSchemeInfo.getSchemeId(),acsSchemeInfo.getAcsOutputText(),acsSchemeInfo.getAcsId());
                            });
                });
        return ResponseDTO.ofSuccess();
    }

    @PostMapping(value = "create/timingScheme")
    @ApiOperation(value = "新建可变车道牌定时方案")
    public ResponseDTO<Void> timingScheme(Integer acsId,Integer index,Integer schemeId){
        createDataService.createTimingScheme(acsId,index,schemeId);
        return ResponseDTO.ofSuccess();
    }

}
