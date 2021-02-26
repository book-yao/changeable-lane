package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.ChangeableLaneArea;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:20
 */
@Mapper
public interface ChangeableLaneAreaMapper {
    List<ChangeableLaneArea> allAreas();

    int insertAreaScheme(@Param("areaId")Integer areaId,
                         @Param("schemeInfo")String schemeInfo);

    int delteAreaScheme(@Param("areaId")Integer areaId);

    String selectAreaSchemeByAreaId(@Param("areaId")Integer areaId);
}
