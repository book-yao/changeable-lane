package com.supcon.changeablelane.controller;

import com.supcon.changeablelane.constant.StatusCode;
import com.supcon.changeablelane.domain.ChangeableLaneArea;
import com.supcon.changeablelane.domain.ChangeableLaneLock;
import com.supcon.changeablelane.domain.Devices;
import com.supcon.changeablelane.domain.TrafficScreen;
import com.supcon.changeablelane.dto.ChangeableLaneLockDTO;
import com.supcon.changeablelane.dto.ResponseDTO;
import com.supcon.changeablelane.mapper.TrafficScreenMapper;
import com.supcon.changeablelane.service.ChangeableLaneAreaService;
import com.supcon.changeablelane.service.ChangeableLaneLockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
        tags = {"信号机接口"})
public class ChangeableLaneAreaController {

    @Autowired
    private ChangeableLaneAreaService changeableLaneAreaService;

    @Autowired
    private ChangeableLaneLockService changeableLaneLockService;

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

    @GetMapping(value = "/variableDriveways")
    @ApiOperation(value = "获取所有的诱导屏")
    public ResponseDTO<List<TrafficScreen>> variableDriveways(){

        List<TrafficScreen> list = changeableLaneAreaService.getAllVariableDriveways();
        return ResponseDTO.ofSuccess(list);
    }


}
