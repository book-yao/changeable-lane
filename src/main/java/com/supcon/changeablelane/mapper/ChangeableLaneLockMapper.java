package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.ChangeableLaneLock;
import org.mapstruct.Mapper;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:36
 */
@Mapper
public interface ChangeableLaneLockMapper {

    /**
     * 获取最新的一条锁定
     * @param areaId
     * @return
     */
    ChangeableLaneLock selectLastLockByAreaId(Integer areaId);

    int deleteChangeableLaneLockByAreaId(Integer areaId);

    int insertChangeableLaneLock(ChangeableLaneLock changeableLaneLock);
}
