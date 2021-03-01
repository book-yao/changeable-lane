package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.TrafficScreen;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:20
 */
@Mapper
public interface TrafficScreenMapper {
    List<TrafficScreen> selectTrafficScreenByAreaId(Integer areaId);

    List<TrafficScreen> selectTrafficScreen();

    List<TrafficScreen> selectTrafficScreenByAcsId(Integer acsId);
}
