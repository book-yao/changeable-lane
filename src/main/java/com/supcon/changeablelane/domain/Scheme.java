package com.supcon.changeablelane.domain;

import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 可变车道协同体方案
 * @Author caowenbo
 * @create 2021/2/24 14:52
 */
@Data
public class Scheme {
    private int id;

    private int schemeId;

    private String name;

    private int areaId;


    List<ChangeableLaneScheme> changeableLaneSchemes;

    /**
     * 获取关键路口方案
     * @param keyAcsId
     * @return
     */
    public List<ChangeableLaneScheme> getKeyAcsScheme(List<Integer> keyAcsId) {
        return this.getChangeableLaneSchemes().stream()
                .filter(item-> keyAcsId.contains(item.getAcsId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取非关键路口方案
     * @param keyAcsId
     * @return
     */
    public List<ChangeableLaneScheme> getAcsScheme(List<Integer> keyAcsId) {
        return this.getChangeableLaneSchemes().stream()
                .filter(item-> !keyAcsId.contains(item.getAcsId()))
                .collect(Collectors.toList());
    }
}
