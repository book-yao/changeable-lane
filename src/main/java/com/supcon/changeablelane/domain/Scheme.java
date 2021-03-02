package com.supcon.changeablelane.domain;

import lombok.Data;
import org.springframework.util.CollectionUtils;

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

    /**
     * 对比方案不同点
     * @param lastScheme
     */
    public void compareOtherScheme(Scheme lastScheme) {
        if (Objects.isNull(lastScheme)|| CollectionUtils.isEmpty(lastScheme.getChangeableLaneSchemes())) {
            return;
        }
        this.getChangeableLaneSchemes().stream()
        .forEach(item->{
            ChangeableLaneScheme otherScheme = lastScheme.getChangeableLaneSchemes().stream()
                    .filter(changeableLaneScheme -> Objects.equals(changeableLaneScheme.getAcsId(), item.getAcsId()))
                    .findFirst()
                    .orElse(null);
            item.compareOtherChangeableLaneScheme(otherScheme);
        });
    }
}
