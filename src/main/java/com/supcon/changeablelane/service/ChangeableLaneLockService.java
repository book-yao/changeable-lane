package com.supcon.changeablelane.service;

import com.supcon.changeablelane.client.TrafficScreenClient;
import com.supcon.changeablelane.client.VariableLaneClient;
import com.supcon.changeablelane.domain.*;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import com.supcon.changeablelane.dto.ChangeableLaneLockDTO;
import com.supcon.changeablelane.dto.OutputSchemesDTO;
import com.supcon.changeablelane.dto.VariableLaneControl;
import com.supcon.changeablelane.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private TrafficScreenClient trafficScreenClient;

    public String areaLock(Integer areaId, ChangeableLaneLockDTO changeableLaneLock) {
        if(Objects.equals(changeableLaneLock.getLockType(),2)){
            changeableLaneLockMapper.deleteChangeableLaneLockByAreaId(areaId);
            this.sendSchemeToOs(areaId,changeableLaneLock.getSchemeId());
        }else{
            ChangeableLaneLock result = changeableLaneLockMapper.selectLastLockByAreaId(areaId);
            if(Objects.nonNull(result)&&result.isValid()){
                return "已处于锁定期内，请先解锁，在重新锁定";
            }
            changeableLaneLockMapper.deleteChangeableLaneLockByAreaId(areaId);
            changeableLaneLock.setAreaId(areaId);
            changeableLaneLockMapper.insertChangeableLaneLock(changeableLaneLock.dto2ChangeableLaneLock(areaId));
            //向信号机下发信号机方案，可变车道牌方案，待转屏方案
            //this.downScheme(areaId,changeableLaneLock.getSchemeId());
        }
        return null;
    }

    /**
     * 下发方案
     * @param areaId
     * @param schemeId
     */
    private void sendSchemeToOs(Integer areaId, Integer schemeId) {

        Scheme scheme = fillScheme(areaId,schemeId);
        //获取信号机方案
        if(Objects.nonNull(scheme)&&!CollectionUtils.isEmpty(scheme.getChangeableLaneSchemes())){
            scheme.getChangeableLaneSchemes().stream()
                    .forEach(item ->{
                        downScheme(item);
                    });
        }
    }

    /**
     * 下发信号机方案，可变车道方案，待转屏方案
     * @param changeableLaneScheme
     */
    private void downScheme(ChangeableLaneScheme changeableLaneScheme) {
        //下发可变车道牌方案
        List<VariableLaneDTO> variableLaneList = changeableLaneScheme.getVariableLaneSchemes();
        VariableLaneControl variableLaneControl = new VariableLaneControl();
        variableLaneControl.setAcsId(changeableLaneScheme.getAcsId());
        variableLaneControl.setVariableLaneList(variableLaneList);
        variableLaneClient.variableLaneControlDown(variableLaneControl);
        //下发信号机方案
        variableLaneClient
                .downOutput(new OutputSchemesDTO(changeableLaneScheme.getAcsOutputs(), 12 * 60));

        //下发待转屏方案
        changeableLaneScheme.getTrafficScreenSchemes().stream()
                .forEach(item->{
                    trafficScreenClient.sendLedMessage(item);
                });
    }

    public Scheme fillScheme(Integer areaId, Integer schemeId) {
        List<AcsInfo> acsInfos = acsMapper.selectAcsByAreaId(areaId);
        Scheme scheme = schemeMapper.selectSchemeByAcsIdAndSchemeId(areaId, schemeId);
        List<ChangeableLaneScheme> changeableLaneSchemes = new ArrayList<>();
        acsInfos.stream()
                .forEach(item->{
                    ChangeableLaneScheme changeableLaneScheme = new ChangeableLaneScheme();
                    changeableLaneScheme.setAcsId(item.getAcsId());
                    //获取所有信号机方案
                    List<AcsSchemeInfo> acsSchemeInfos = acsSchemeMapper.selectAcsSchemeByAcsIdAndSchemeId(item.getAcsId(), scheme.getId());
                    if(!CollectionUtils.isEmpty(acsSchemeInfos)){
                        AcsSchemeInfo acsSchemeInfo = acsSchemeInfos.get(0);
                        changeableLaneScheme.setAcsSchemeInfo(acsSchemeInfo);
                    }else{
                        //设置单点方案
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
}
