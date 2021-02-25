package com.supcon.changeablelane.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author lidewen
 * @date 2018/8/28
 * @description 方案场景模式，分为高、中、低三种负荷
 */
public enum SchemeModeEnum {
    HIGH_LOAD(1, "高负荷"),
    MIDDLE_LOAD(2, "中负荷"),
    LOW_LOAD(3, "低负荷");

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    SchemeModeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static SchemeModeEnum getItem(Integer id) {
        for (SchemeModeEnum item : values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
