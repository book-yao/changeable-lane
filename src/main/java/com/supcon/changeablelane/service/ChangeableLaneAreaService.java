package com.supcon.changeablelane.service;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.domain.*;
import com.supcon.changeablelane.domain.scheme.PhaseScheme;
import com.supcon.changeablelane.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:19
 */
@Service
@Slf4j
public class ChangeableLaneAreaService {

    @Resource
    private ChangeableLaneAreaMapper changeableLaneAreaMapper;

    @Resource
    private ChangeableLaneLockMapper changeableLaneLockMapper;

    @Resource
    private VariableDrivewayMapper variableDrivewayMapper;

    @Resource
    private TrafficScreenMapper trafficScreenMapper;

    @Resource
    private AcsMapper acsMapper;

    @Autowired
    private ChangeableLaneLockService changeableLaneLockService;

    public List<ChangeableLaneArea> areas() {
        List<ChangeableLaneArea> result = changeableLaneAreaMapper.allAreas();
        //设置每个协同体的锁定状态
        result.stream()
                .forEach(item ->{
                    ChangeableLaneLock changeableLaneLock = changeableLaneLockMapper.selectLastLockByAreaId(item.getAreaId());
                    item.handlerStatus(changeableLaneLock);
                });
        return result;
    }

    public Devices devicesByAreaId(Integer areaId) {
        Devices devices = new Devices();
        try {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> this.handleAcsInfos(areaId, devices)),
                    CompletableFuture.runAsync(() -> this.handleVariableDriveways(areaId, devices)),
                    CompletableFuture.runAsync(() -> this.handleTrafficScreens(areaId, devices))
            ).get();
        } catch (Exception e) {
            log.info("{},获取失败，失败原因：{}",areaId,e);
        }
        return devices;
    }

    private void handleAcsInfos(Integer areaId, Devices devices) {
        List<AcsInfo> acsInfos = acsMapper.selectAcsByAreaId(areaId);
        devices.setAcsInfos(acsInfos);
    }

    private void handleVariableDriveways(Integer areaId, Devices devices) {
        List<VariableDriveway> variableDriveways = variableDrivewayMapper.selectVariableDrivewayByAreaId(areaId);
        devices.setVarLaneCards(variableDriveways);
    }

    private void handleTrafficScreens(Integer areaId, Devices devices) {
        List<TrafficScreen> trafficScreens = trafficScreenMapper.selectTrafficScreenByAreaId(areaId);
        devices.setTrafficScreens(trafficScreens);
    }

    public List<TrafficScreen> getAllVariableDriveways() {
        return trafficScreenMapper.selectTrafficScreen();
    }

    public Scheme getLastScheme(Integer areaId) {
        String schemeStr = changeableLaneAreaMapper.selectAreaSchemeByAreaId(areaId);
        if(Objects.isNull(schemeStr)){
            return changeableLaneLockService.insertRunningSchemeHis(areaId);
        }
        JSONObject jsonObject = JSONObject.parseObject(schemeStr);
        return jsonObject.toJavaObject(Scheme.class);
    }

    public Scheme getRunningScheme(Integer areaId) {
       return changeableLaneLockService.insertRunningSchemeHis(areaId);
    }
}
