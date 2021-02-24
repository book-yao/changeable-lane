package com.supcon.changeablelane.service;

import com.supcon.changeablelane.domain.ChangeableLaneLock;
import com.supcon.changeablelane.dto.ChangeableLaneLockDTO;
import com.supcon.changeablelane.mapper.ChangeableLaneLockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public String areaLock(Integer areaId, ChangeableLaneLockDTO changeableLaneLock) {
        if(Objects.equals(changeableLaneLock.getLockType(),2)){
            changeableLaneLockMapper.deleteChangeableLaneLockByAreaId(areaId);
        }else{
            ChangeableLaneLock result = changeableLaneLockMapper.selectLastLockByAreaId(areaId);
            if(Objects.nonNull(result)&&result.isValid()){
                return "已处于锁定期内，请先解锁，在重新锁定";
            }
            changeableLaneLockMapper.deleteChangeableLaneLockByAreaId(areaId);
            changeableLaneLock.setAreaId(areaId);
            changeableLaneLockMapper.insertChangeableLaneLock(changeableLaneLock.dto2ChangeableLaneLock(areaId));
        }
        return null;
    }
}
