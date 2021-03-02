package com.supcon.changeablelane.service;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.DataTrafficClient;
import com.supcon.changeablelane.client.TrafficScreenClient;
import com.supcon.changeablelane.client.VariableLaneClient;
import com.supcon.changeablelane.client.dto.VariableLaneStateDTO;
import com.supcon.changeablelane.client.dto.VariableLaneStatesDTO;
import com.supcon.changeablelane.domain.*;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.dto.ChangeableLaneLockDTO;
import com.supcon.changeablelane.dto.OutputSchemesDTO;
import com.supcon.changeablelane.dto.VariableLaneControl;
import com.supcon.changeablelane.mapper.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:27
 */
@Service
@Slf4j
public class ChangeableLaneLockService {

    @Resource
    private ChangeableLaneLockMapper changeableLaneLockMapper;

    @Resource
    private VariableLaneSchemeMapper variableLaneSchemeMapper;

    @Autowired
    private VariableLaneClient variableLaneClient;

    @Resource
    private AcsMapper acsMapper;

    @Resource
    private SchemeMapper schemeMapper;

    @Resource
    private AcsSchemeMapper acsSchemeMapper;

    @Resource
    private TrafficScreenSchemeMapper trafficScreenSchemeMapper;

    @Autowired
    private TrafficScreenClient trafficScreenClient;

    @Autowired
    private DataTrafficClient dataTrafficClient;

    @Resource
    private ChangeableLaneAreaMapper changeableLaneAreaMapper;

    @Value("#{${param.acsId-intersectionId}}")
    private Map<Integer,Integer> acsIdIntersectionIdMap;

    @Resource
    private TrafficScreenMapper trafficScreenMapper;

    @Value("#{'${param.keyAcsId}'.split(',')}")
    private List<Integer> keyAcsId;

    @Autowired
    @Qualifier("executorService")
    private ExecutorService executorService;

    private Integer acsLockTime = 50;


    public String areaLock(Integer areaId, ChangeableLaneLockDTO changeableLaneLock) {

        if(Objects.equals(changeableLaneLock.getLockType(),2)){
            changeableLaneLockMapper.deleteChangeableLaneLockByAreaId(areaId);
            executorService.execute(()->{
                this.sendSchemeToOs(areaId,changeableLaneLock.getSchemeId(),changeableLaneLock.getLockTime(),1);
            });

        }else{
            Scheme scheme = schemeMapper.selectSchemeByAreaIdAndSchemeId(areaId, changeableLaneLock.getSchemeId());
            if(Objects.isNull(scheme)){
                return "锁定方案不存在";
            }
            ChangeableLaneLock result = changeableLaneLockMapper.selectLastLockByAreaId(areaId);
            if(Objects.nonNull(result)&&result.isValid()){
                return "已处于锁定期内，请先解锁，在重新锁定";
            }
            changeableLaneLockMapper.deleteChangeableLaneLockByAreaId(areaId);
            changeableLaneLock.setAreaId(areaId);
            changeableLaneLockMapper.insertChangeableLaneLock(changeableLaneLock.dto2ChangeableLaneLock(areaId));
            //向信号机下发信号机方案，可变车道牌方案，待转屏方案
            executorService.execute(()->{
                this.sendSchemeToOs(areaId,changeableLaneLock.getSchemeId(),changeableLaneLock.getLockTime(),2);
            });
        }
        return null;
    }


    //获取当前信号机，可变车道牌，诱导屏 正在运行的方案
    public Scheme insertRunningSchemeHis(Integer areaId){
        List<AcsInfo> acsInfos = acsMapper.selectAcsByAreaId(areaId);
        Scheme scheme = new Scheme();
        scheme.setAreaId(areaId);
        scheme.setName("历史方案");
        List<ChangeableLaneScheme> changeableLaneSchemes = new ArrayList<>();
        acsInfos.stream()
                .forEach(item->{
                    ChangeableLaneScheme changeableLaneScheme = new ChangeableLaneScheme();
                    //获取信号机运行记录
                    AcsOutput acsOutput = dataTrafficClient.getLastRunningScheme(item.getAcsId());
                    changeableLaneScheme.setAcsOutputs(acsOutput);
                    changeableLaneScheme.setAcsId(item.getAcsId());
                    changeableLaneScheme.setAcsName(item.getName());
                    changeableLaneScheme.setIntersectionId(acsIdIntersectionIdMap.get(item.getAcsId()));
                    //获取到可变车道牌运行记录
                    //Integer intersectionId = acsIdIntersectionIdMap.get(item.getAcsId());
                    if(Objects.nonNull(item.getAcsId())){
                        variableLaneClient.getVariableLaneState(item.getAcsId())
                                .ifPresent(variableLaneStatesDTO -> {
                                    if(Objects.nonNull(variableLaneStatesDTO)&&!CollectionUtils.isEmpty(variableLaneStatesDTO.getLaneStates())){
                                        List<VariableLaneDTO> variableLaneList = variableLaneStatesDTO.convertToVariableLaneControlByNoChangeLane(
                                                item.getAcsId()).getVariableLaneList();
                                        variableLaneList.stream()
                                                .forEach(variableLaneDTO->{
                                                    variableLaneDTO.setIntersectionId(item.getAcsId());
                                                });
                                        changeableLaneScheme.setVariableLaneSchemes(variableLaneList);
                                    }

                                });
                    }
                    //获取诱导屏信息
                    changeableLaneSchemes.add(changeableLaneScheme);
                });
        scheme.setChangeableLaneSchemes(changeableLaneSchemes);
        changeableLaneAreaMapper.delteAreaScheme(areaId);
        changeableLaneAreaMapper.insertAreaScheme(areaId, JSONObject.toJSONString(scheme));
        return scheme;
    }

    /**
     * 下发指定方案到os
     * type 1 单点方案，2 是算法方案
     * @param areaId
     * @param schemeId
     */
    @SneakyThrows
    private void sendSchemeToOs(Integer areaId, Integer schemeId, Integer lockTime, Integer type) {
        //先获取当前区域运行信号机，可变车道牌,诱导屏方案
        insertRunningSchemeHis(areaId);
        Scheme scheme ;
        if(Objects.equals(type,1)){
            scheme = fillSingleScheme(areaId,schemeId);
        }else{
            scheme = fillScheme(areaId,schemeId);
        }
        //区分出关键路口方案以及相领路口方案
        List<ChangeableLaneScheme> keyAcsSchemes = scheme.getKeyAcsScheme(keyAcsId);
        List<ChangeableLaneScheme> acsSchemes = scheme.getAcsScheme(keyAcsId);

        List<Future<Boolean>> result = new ArrayList<>();

        //下发关键路口的方案
        keyAcsSchemes.stream()
                .forEach(item->{
                    Future<Boolean> submit = executorService.submit(() -> {
                        return downScheme(item,lockTime);
                    });
                    result.add(submit);
                });

        for (Future<Boolean> res:result) {
            Boolean aBoolean = false;
            try {
                aBoolean = res.get();
            } catch (Exception e) {
                log.error("可变车道牌 关键路口方案下发失败,流程结束");
            }
            if(!aBoolean){
                log.error("可变车道牌 关键路口方案下发失败,流程结束");
                return;
            }
        }
        //下发关联路口的方案
        acsSchemes.stream()
                .forEach(item->{
                    Future<Boolean> submit = executorService.submit(() -> {
                        return downScheme(item,lockTime);
                    });
                });
    }

    /**
     * 下发信号机方案，可变车道方案，待转屏方案
     * @param changeableLaneScheme
     */
    private boolean downScheme(ChangeableLaneScheme changeableLaneScheme,Integer lockTime) {
        //下发待转屏方案
        List<Boolean> booleans= new ArrayList<>();
        changeableLaneScheme.getTrafficScreenSchemes().stream()
                .filter(Objects::nonNull)
                .forEach(item->{
                    boolean b = trafficScreenClient.sendLedMessage(item);
                    booleans.add(b);
                });
        for (Boolean res:booleans) {
            if(!res){
                log.error("诱导屏方案下发失败,流程结束");
                return false;
            }
        }

        //下发可变车道牌临时方案
        List<VariableLaneDTO> variableLaneList = changeableLaneScheme.getVariableLaneSchemesByType(1);
        VariableLaneControl variableLaneControl;
        boolean isSend ;
        if(CollectionUtils.isEmpty(variableLaneList)){
             variableLaneControl =
                    VariableLaneControl.builder()
                            .acsId(changeableLaneScheme.getAcsId())
                            .variableLaneList(
                                    Arrays.asList(VariableLaneDTO.builder().mode(4).entranceId(null).build()))
                            .build();

             variableLaneClient.variableLaneControlDown(variableLaneControl);
             isSend = true;
        }else{
            variableLaneControl = new VariableLaneControl (changeableLaneScheme.getAcsId(),variableLaneList);
            variableLaneClient.variableLaneControlDown(variableLaneControl);
            //判断可变车道牌是否已经
            isSend = judgeSchemeIsSend(changeableLaneScheme.getAcsId(), variableLaneList);
            if(!isSend){
                log.error("可变车道牌 | {} 下发失败",changeableLaneScheme.getAcsId());
                return false;
            }

        }


        log.info("可变车道牌 | {}，临时方案下发成功",changeableLaneScheme.getAcsId());
        //下发信号机方案
        variableLaneClient
                .downOutput(new OutputSchemesDTO(changeableLaneScheme.getAcsId(),changeableLaneScheme.getAcsOutputs(),lockTime, 12 * 60));

        //等待信号方案一个周期
        try {
            log.info("路口 | {}，信号机方案下发成功，进入等待时间，等待（{}）秒",
                    changeableLaneScheme.getAcsId(),
                    acsLockTime);
            Thread.sleep(acsLockTime*1000);
        } catch (InterruptedException e) {
            log.error("服务异常：{}",e);
            return false;
        }

        //下发可变车道牌方案
        log.info("可变车道牌 | {}，正式方案开始下发",changeableLaneScheme.getAcsId());
        List<VariableLaneDTO> variableLaneDTOS = changeableLaneScheme.getVariableLaneSchemesByType(2);
        if(!CollectionUtils.isEmpty(variableLaneDTOS)){
            variableLaneControl = new VariableLaneControl
                    (changeableLaneScheme.getAcsId(),variableLaneDTOS,lockTime);
            variableLaneClient.variableLaneControlDown(variableLaneControl);
            isSend = judgeSchemeIsSend(changeableLaneScheme.getAcsId(), variableLaneDTOS);
        }else{
            variableLaneControl =
                    VariableLaneControl.builder()
                            .acsId(changeableLaneScheme.getAcsId())
                            .variableLaneList(
                                    Arrays.asList(VariableLaneDTO.builder().mode(4).entranceId(null).build()))
                            .build();
            variableLaneClient.variableLaneControlDown(variableLaneControl);
            isSend = true;
        }

        log.info("可变车道牌 | {}，正式方案下发成功",changeableLaneScheme.getAcsId());
        return isSend;
    }

    private boolean judgeSchemeIsSend(Integer acsId,List<VariableLaneDTO> variableLaneList) {
        boolean isSuccess = false;
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(120);
        while(true){
            VariableLaneStatesDTO variableLaneStatesDTO = variableLaneClient.getVariableLaneState(acsId)
                    .orElse(null);
            if(Objects.nonNull(variableLaneStatesDTO)){
                List<VariableLaneStateDTO> schemeRecord = variableLaneStatesDTO.getLaneStates();
                //判断方案是否已经执行
                boolean isSame = judgeSchemeIsSame(variableLaneList, schemeRecord);
                if(isSame){
                    isSuccess = true;
                    break;
                }else if(LocalDateTime.now().isBefore(endTime)){
                    //每隔1秒获取一次可变车道牌状态
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    //超过30秒。跳出循环
                    break;
                }
            }
        }
        return isSuccess;
    }

    private boolean judgeSchemeIsSame(List<VariableLaneDTO> variableLaneList, List<VariableLaneStateDTO> schemeRecord) {
        boolean isSame = true;
        if(CollectionUtils.isEmpty(schemeRecord)){
            return false;
        }
        for(VariableLaneDTO dto : variableLaneList){
            VariableLaneStateDTO variableLaneStateDTO = schemeRecord.stream()
                    .filter(item -> Objects.equals(item.getEntranceId(), dto.getEntranceId())&&
                            Objects.equals(item.getLaneId(),dto.getLaneId()))
                    .findFirst()
                    .orElse(null);
            if(Objects.isNull(variableLaneStateDTO)
                    ||!Objects.equals(dto.getState(),variableLaneStateDTO.getStatus())){
                isSame = false;
                break;
            }
        }
        return isSame;
    }

    public Scheme fillScheme(Integer areaId, Integer schemeId) {
        List<AcsInfo> acsInfos = acsMapper.selectAcsByAreaId(areaId);
        Scheme scheme = schemeMapper.selectSchemeByAreaIdAndSchemeId(areaId, schemeId);
        List<ChangeableLaneScheme> changeableLaneSchemes = new ArrayList<>();
        acsInfos.stream()
                .forEach(item->{
                    ChangeableLaneScheme changeableLaneScheme = new ChangeableLaneScheme();
                    changeableLaneScheme.setAcsId(item.getAcsId());
                    changeableLaneScheme.setAcsName(item.getName());
                    changeableLaneScheme.setIntersectionId(acsIdIntersectionIdMap.get(item.getAcsId()));
                    //获取所有信号机方案
                    List<AcsSchemeInfo> acsSchemeInfos = acsSchemeMapper.selectAcsSchemeByAcsIdAndSchemeId(item.getAcsId(), scheme.getId());
                    if(!CollectionUtils.isEmpty(acsSchemeInfos)){
                        AcsSchemeInfo acsSchemeInfo = acsSchemeInfos.get(0);
                        changeableLaneScheme.setAcsSchemeInfo(acsSchemeInfo);
                    }
                    //获取所有可变车道方案
                    List<VariableLaneDTO> variableLaneDTO = variableLaneSchemeMapper.selectVariableLaneSchemeBySchemeIdAndAcsId(scheme.getId(), item.getAcsId());
                    changeableLaneScheme.setVariableLaneSchemes(variableLaneDTO);
                    //获取所有待转屏方案
                    List<TrafficScreenScheme> trafficScreenSchemes = trafficScreenSchemeMapper.selectTrafficScreenSchemeByAreaId(scheme.getId(), item.getAcsId());
                    changeableLaneScheme.setTrafficScreenSchemes(trafficScreenSchemes);
                    changeableLaneSchemes.add(changeableLaneScheme);
                });
        scheme.setChangeableLaneSchemes(changeableLaneSchemes);
        return scheme;
    }

    public Scheme fillSingleScheme(Integer areaId, Integer schemeId) {
        List<AcsInfo> acsInfos = acsMapper.selectAcsByAreaId(areaId);
        Scheme scheme = new Scheme();
        scheme.setAreaId(areaId);
        scheme.setSchemeId(schemeId);
        List<ChangeableLaneScheme> changeableLaneSchemes = new ArrayList<>();
        acsInfos.stream()
                .forEach(item->{
                    ChangeableLaneScheme changeableLaneScheme = new ChangeableLaneScheme();
                    changeableLaneScheme.setAcsId(item.getAcsId());
                    //获取所有信号机方案
                    AcsOutput acsOutput = new AcsOutput();
                    acsOutput.setAcsId(item.getAcsId());
                    acsOutput.resetSingle();
                    changeableLaneScheme.setAcsOutputs(acsOutput);
                    //获取所有可变车道方案
                    VariableLaneControl variableLaneControl =
                            VariableLaneControl.builder()
                                    .acsId(item.getAcsId())
                                    .variableLaneList(
                                            Arrays.asList(VariableLaneDTO.builder().mode(4).entranceId(null).build()))
                                    .build();
                    changeableLaneScheme.setVariableLaneSchemes(variableLaneControl.getVariableLaneList());
                    //获取所有待转屏单点方案方案
                    List<TrafficScreen> trafficScreens = trafficScreenMapper.selectTrafficScreenByAreaId(areaId);
                    List<TrafficScreenScheme> collect = trafficScreens.stream()
                            .map(trafficScreen -> {
                                return trafficScreen.singleScheme();
                            }).collect(Collectors.toList());
                    changeableLaneScheme.setTrafficScreenSchemes(collect);
                    changeableLaneSchemes.add(changeableLaneScheme);
                });
        scheme.setChangeableLaneSchemes(changeableLaneSchemes);
        return scheme;
    }

    public ChangeableLaneLock getLock(Integer areaId) {
        ChangeableLaneLock result = changeableLaneLockMapper.selectLastLockByAreaId(areaId);
        if(Objects.nonNull(result)&&result.isValid()){
           return result;
        }
        return null;
    }

    public void updateLockTime(Integer lockTime) {
        this.acsLockTime = lockTime;
    }
}
