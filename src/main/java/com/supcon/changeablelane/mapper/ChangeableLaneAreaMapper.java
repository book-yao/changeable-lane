package com.supcon.changeablelane.mapper;

import com.supcon.changeablelane.domain.ChangeableLaneArea;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author caowenbo
 * @create 2021/2/24 16:20
 */
@Mapper
public interface ChangeableLaneAreaMapper {
    List<ChangeableLaneArea> allAreas();
}
