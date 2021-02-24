package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.AcsInfo;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:00
 */
public interface AcsMapper {
   List<AcsInfo> selectAcsByAreaId(Integer areaId);
}
