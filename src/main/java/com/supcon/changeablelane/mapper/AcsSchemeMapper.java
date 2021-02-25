package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.AcsInfo;
import com.supcon.changeablelane.domain.scheme.AcsSchemeInfo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:00
 */
@Mapper
public interface AcsSchemeMapper {
   List<AcsSchemeInfo> selectAcsSchemeByAcsIdAndSchemeId(Integer acsId, Integer schemeId);

   int insertAcsScheme(@Param("schemeId") Integer schemeId,@Param("acsSchemeInfo")String acsSchemeInfo,@Param("acsId") Integer acsId);
}
