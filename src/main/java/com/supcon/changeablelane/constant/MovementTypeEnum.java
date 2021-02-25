package com.supcon.changeablelane.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author lidewen
 * @date 2019/3/14
 */
public enum MovementTypeEnum {
    UNKNOWN(0, "未知"),
    LEFT(1, "左转"),
    STRAIGHT(2, "直行"),
    RIGHT(3, "右转"),
    WALK(4, "人行"),
    LEFT_STRAIGHT(5, "左直行"),
    STRAIGHT_RIGHT(6, "直右行"),
    LEFT_STRAIGHT_RIGHT(7, "通行"),
    TURN_BACK(8, "调头"),
    LEFT_TURN_BACK(9, "左调头"),
    WALK_ONE(10, "人行1"),
    WALK_TWO(11, "人行2"),
    LEFT_NON_MOTOR(12, "非机动车左"),
    STRAIGHT_NON_MOTOR(13, "非机动车直"),
    LEFT_STRAIGHT_NON_MOTOR(14, "非机动车左直"),
    CHANGEABLE(15, "可变车道");


    /**
     * 变化类型的id
     */
    private Integer id;

    /**
     * 变化类型的名称
     */
    private String name;

    MovementTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static MovementTypeEnum getItem(Integer id) {
        for (MovementTypeEnum item : values()) {
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
