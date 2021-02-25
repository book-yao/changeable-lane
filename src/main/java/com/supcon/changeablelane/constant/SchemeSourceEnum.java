package com.supcon.changeablelane.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 算法厂商
 *
 * @author lidewen
 * @date 2019/4/30
 */

public enum SchemeSourceEnum {
    UNKNOWN(0, "未知"),
    SUPCONIT(1, "中控信息");

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    SchemeSourceEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static SchemeSourceEnum getItem(Integer id) {
        for (SchemeSourceEnum item : values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public boolean isUnknown() {
        return this.equals(UNKNOWN);
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
