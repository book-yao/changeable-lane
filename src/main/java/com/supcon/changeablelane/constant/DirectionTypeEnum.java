package com.supcon.changeablelane.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author lidewen
 * @date 2019/4/26
 */
public enum DirectionTypeEnum {
    UNKNOWN(0, "未知"),
    EAST(1, "东"),
    SOUTH(2, "南"),
    WEST(3, "西"),
    NORTH(4, "北"),
    DIRECTION1(5, "方向1"),
    DIRECTION2(6, "方向2"),
    DIRECTION3(7, "方向3"),
    DIRECTION4(8, "方向4"),
    DIRECTION5(9, "方向5"),
    CHANGEABLE(15, "可变车道"),
    WAITING(14, "待转屏");

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    DirectionTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static DirectionTypeEnum getItem(Integer id) {
        for (DirectionTypeEnum item : values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public DirectionTypeEnum getOpposite() {
        switch (this) {
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case NORTH:
                return SOUTH;
            default:
                return UNKNOWN;
        }
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
