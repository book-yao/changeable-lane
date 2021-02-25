package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.Scheme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:00
 */
public interface SchemeMapper {
   Scheme selectSchemeByAcsIdAndSchemeId(@Param(value = "areaId") Integer areaId,
                                               @Param(value = "schemeId")Integer schemeId);
}
