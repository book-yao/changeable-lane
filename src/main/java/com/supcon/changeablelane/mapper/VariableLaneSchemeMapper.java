package com.supcon.changeablelane.mapper;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.domain.VariableDriveway;
import com.supcon.changeablelane.domain.VariableLaneDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:00
 */
public interface VariableLaneSchemeMapper {

   /**
    *
    * @param list
    * @return
    */
   int insertVariableLaneScheme(List<VariableLaneDTO> list);

   List<VariableLaneDTO> selectVariableLaneSchemeBySchemeIdAndAcsId(@Param("schemeId")Integer schemeId,
                                                              @Param("acsId")Integer acsId);

    int insertVariableLane(List<JSONObject> list);
}
