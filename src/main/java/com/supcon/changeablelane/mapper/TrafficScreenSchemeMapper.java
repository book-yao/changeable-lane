package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.TrafficScreenScheme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:00
 */
public interface TrafficScreenSchemeMapper {
   List<TrafficScreenScheme> selectTrafficScreenSchemeByAreaId(@Param("schemeId") Integer schemeId,
                                                               @Param("acsId")Integer acsId);
}
