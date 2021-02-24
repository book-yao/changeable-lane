package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.VariableDriveway;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 18:10
 */
@Mapper
public interface VariableDrivewayMapper {

    List<VariableDriveway> selectVariableDrivewayByAreaId(Integer areaId);
}
