package com.supcon.changeablelane.controller;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.DataTrafficClient;
import com.supcon.changeablelane.client.dto.AcsConfigInfoDTO;
import com.supcon.changeablelane.constant.StatusCode;
import com.supcon.changeablelane.domain.Scheme;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import com.supcon.changeablelane.dto.ResponseDTO;
import com.supcon.changeablelane.mapper.AcsSchemeMapper;
import com.supcon.changeablelane.mapper.SchemeMapper;
import com.supcon.changeablelane.service.CreateDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
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

    @Resource
    private SchemeMapper schemeMapper;

    @Autowired
    private CreateDataService createDataService;


    @Value("#{${param.acsId-intersectionId}}")
    private Map<Integer,Integer> acsIdIntersectionIdMap;

    @PostMapping(value = "create/acsScheme")
    @ApiOperation(value = "新建信号机方案")
    public ResponseDTO<Void> areas(Integer acsId,
                                   Integer areaId,
                                   Integer index,
                                   Integer schemeId){
        Optional<AcsConfigInfoDTO> acsSingleScheme = dataTrafficClient.getAcsSingleScheme(acsId);

        Scheme scheme = schemeMapper.selectSchemeByAreaIdAndSchemeId(areaId, schemeId);
        if(Objects.isNull(scheme)){
            return ResponseDTO.ofError(StatusCode.CODE_SOURCE_NOT_FOUND,"schemeId不存在。");
        }
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
                                acsSchemeInfo.setSchemeId(scheme.getId());
                                acsSchemeInfo.setAcsOutputText(JSONObject.toJSONString(phaseScheme));
                                acsSchemeMapper.insertAcsScheme(acsSchemeInfo.getSchemeId(),acsSchemeInfo.getAcsOutputText(),acsSchemeInfo.getAcsId());
                            });
                });
        return ResponseDTO.ofSuccess();
    }

    @PostMapping(value = "create/timingScheme")
    @ApiOperation(value = "新建可变车道牌定时方案")
    public ResponseDTO<Void> timingScheme(Integer areaId,
                                          Integer acsId,
                                          Integer index,
                                          Integer schemeId){
        Scheme scheme = schemeMapper.selectSchemeByAreaIdAndSchemeId(areaId, schemeId);
        if(Objects.isNull(scheme)){
            return ResponseDTO.ofError(StatusCode.CODE_SOURCE_NOT_FOUND,"schemeId不存在。");
        }
        createDataService.createTimingScheme(acsId,index,scheme.getSchemeId());
        return ResponseDTO.ofSuccess();
    }

}
