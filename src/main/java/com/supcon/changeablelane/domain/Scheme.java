package com.supcon.changeablelane.domain;

import lombok.Data;

import java.util.List;

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

}
