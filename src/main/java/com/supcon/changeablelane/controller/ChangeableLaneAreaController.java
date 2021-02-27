package com.supcon.changeablelane.controller;

import com.supcon.changeablelane.constant.StatusCode;
import com.supcon.changeablelane.domain.*;
import com.supcon.changeablelane.dto.ChangeableLaneLockDTO;
import com.supcon.changeablelane.dto.ResponseDTO;
import com.supcon.changeablelane.mapper.TrafficScreenMapper;
import com.supcon.changeablelane.service.ChangeableLaneAreaService;
import com.supcon.changeablelane.service.ChangeableLaneLockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:17
 */

@RequestMapping(value = "/api/v1/changeableLane/")
@RestController
@Api(
        value = "AcsController",
        tags = {"可变车道协同体接口"})
public class ChangeableLaneAreaController {

    @Autowired
    private ChangeableLaneAreaService changeableLaneAreaService;

    @Autowired
    private ChangeableLaneLockService changeableLaneLockService;

    /**
     * 数据交换url
     */
    @Value("${param.acss}")
    private String acss;


    @GetMapping(value = "areas")
    @ApiOperation(value = "获取所有可变车道协同体列表")
    public ResponseDTO<List<ChangeableLaneArea>> areas(){
        List<ChangeableLaneArea> areas= changeableLaneAreaService.areas();
        return ResponseDTO.ofSuccess(areas);
    }


    @GetMapping(value = "/{areaId}/device")
    @ApiOperation(value = "根据某个指定的协同体id获取所有的设备")
    @ApiResponse(code = 200, message = "获取成功", response = ResponseDTO.class)
    public ResponseDTO<Devices> devices(
            @PathVariable Integer areaId){
        Devices device= changeableLaneAreaService.devicesByAreaId(areaId);
        return ResponseDTO.ofSuccess(device);
    }


    @PostMapping(value = "/{areaId}/lock")
    @ApiOperation(value = "解锁和锁定方案")
    public ResponseDTO<Void> areaLock(
            @PathVariable("areaId")Integer areaId,
            @RequestBody ChangeableLaneLockDTO changeableLaneLock){
        if(Objects.isNull(changeableLaneLock)
           ||Objects.isNull(changeableLaneLock.getLockType())){
            return ResponseDTO.ofError(StatusCode.CODE_PARAM_ERROR,"参数不合法");
        }
        String message = changeableLaneLockService.areaLock(areaId, changeableLaneLock);
        if(Objects.nonNull(message)){
            return ResponseDTO.ofError(StatusCode.CODE_SERVER_EXCEPTION,message);
        }
        return ResponseDTO.ofSuccess();
    }

    @GetMapping(value = "/{areaId}/lock")
    @ApiOperation(value = "获取当前锁定方案")
    public ResponseDTO<ChangeableLaneLock> getLock(
            @PathVariable("areaId")Integer areaId){

        ChangeableLaneLock changeableLaneLock = changeableLaneLockService.getLock(areaId);

        return ResponseDTO.ofSuccess(changeableLaneLock);
    }

    @GetMapping(value = "/trafficSrceen")
    @ApiOperation(value = "获取所有的诱导屏")
    public ResponseDTO<List<TrafficScreen>> variableDriveways(){

        List<TrafficScreen> list = changeableLaneAreaService.getAllVariableDriveways();
        return ResponseDTO.ofSuccess(list);
    }


    @GetMapping(value = "/{areaId}/{schemeId}")
    @ApiOperation(value = "获取指定协同体指定方案信息")
    public ResponseDTO<Scheme> getScheme(@PathVariable("areaId")Integer areaId,
                                         @PathVariable("schemeId")Integer schemeId){

        Scheme scheme = changeableLaneLockService.fillScheme(areaId,schemeId);
        return ResponseDTO.ofSuccess(scheme);
    }

    @GetMapping(value = "/acss")
    @ApiOperation(value = "获取指定信号机")
    public ResponseDTO<List<Integer>> acss(){
        List<Integer> acs = new ArrayList<>();
        if(Objects.nonNull(acss)){
            for(String item:acss.split(",")){
                acs.add(Integer.parseInt(item));
            }
        }
        return ResponseDTO.ofSuccess(acs);
    }

    @GetMapping(value = "/lastRunningScheme/{areaId}")
    @ApiOperation(value = "获取区域历史运行记录")
    public ResponseDTO<Scheme> getLastScheme(@PathVariable("areaId")Integer areaId){
        Scheme scheme= changeableLaneAreaService.getLastScheme(areaId);
        return ResponseDTO.ofSuccess(scheme);
    }

    @GetMapping(value = "/runningScheme/{areaId}")
    @ApiOperation(value = "获取区域正在运行的方案信息")
    public ResponseDTO<Scheme> getRunningScheme(@PathVariable("areaId")Integer areaId){
        Scheme scheme= changeableLaneAreaService.getRunningScheme(areaId);
        return ResponseDTO.ofSuccess(scheme);
    }

}
