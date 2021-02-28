package com.supcon.changeablelane.controller;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.DataTrafficClient;
import com.supcon.changeablelane.client.dto.AcsConfigInfoDTO;
import com.supcon.changeablelane.constant.StatusCode;
import com.supcon.changeablelane.domain.Scheme;
import com.supcon.changeablelane.domain.VariableDriveway;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import com.supcon.changeablelane.dto.ResponseDTO;
import com.supcon.changeablelane.mapper.AcsSchemeMapper;
import com.supcon.changeablelane.mapper.SchemeMapper;
import com.supcon.changeablelane.service.CreateDataService;
import com.supcon.changeablelane.utils.GsonUtil;
import io.swagger.annotations.*;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:17
 */

@RequestMapping(value = "/api/v1/changeableLane/")
@RestController
@Api(
        value = "AcsController",
        tags = {"建造演示数据"})
@Slf4j
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
                                          Integer schemeId,
                                          Integer type){
        Scheme scheme = schemeMapper.selectSchemeByAreaIdAndSchemeId(areaId, schemeId);
        if(Objects.isNull(scheme)){
            return ResponseDTO.ofError(StatusCode.CODE_SOURCE_NOT_FOUND,"schemeId不存在。");
        }
        createDataService.createTimingScheme(acsId,index,scheme.getId(),type);
        return ResponseDTO.ofSuccess();
    }

    @PostMapping(
            value = "/variableLaneCard",
            consumes = "multipart/*",
            produces = "application/json",
            headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传子区路径文件数据", httpMethod = "POST")
    @ApiResponse(code = 200, message = "上传成功")
    @Transactional(rollbackFor = Exception.class)
    public void upLoad(@ApiParam("file") @RequestParam(value = "file") MultipartFile file)
            throws IOException {

        if (file == null) {
            log.info("上传文件为空");
            return;
        }
        MultipartFile mpf = file;
            try{
                @Cleanup
                BufferedReader streamReader =
                        new BufferedReader(new InputStreamReader(mpf.getInputStream(), "utf-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
                List<JSONObject> result = (List<JSONObject>)GsonUtil.fromJson(responseStrBuilder.toString(), List.class);
                createDataService.insertIntoVariableLaneCard(result);
            }catch (Exception e){
                log.error("可变车道牌信息导入出错,{}",e);
            }
    }

}
