package com.supcon.changeablelane.constant;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 算法模式枚举
 *
 * @author JWF
 */
@JSONType(serializeEnumAsJavaBean = true, serializer = EnumSerializer.class)
public enum AlgorithmModeEnum implements DisplayedEnum, IEnum {
    UNKNOWN(0, "未知"),
    SCHEME(1, "子区方案生成器"),
    SMART(2, "子区方案知识库"),
    SINGLE(3, "单点方案"),
    SINGLE_ADAPTIVE(4, "单点自适应"),
    VARIABLE_LANE(5, "可变车道"),
    SINGLE_STOP(6, "单点停止"),
    SINGLE_SMART(7, "单点知识库"),
    ALARM_SCHEME(8, "报警方案"),
    EXPERT_DOWNLOAD_SCHEME(9, "专家自定下发方案"),
    EXPERT_SAVE_SCHEME(10, "专家自定保存方案"),
    RAMP_TIMING_CONTROL(11, "匝道定时控制"),
    RAMP_INTELLIGENT_CONTROL(12, "匝道智能控制"),
    SPLIT_SCHEME(13, "绿信比算法方案"),
    AI_TRUSTEESHIP_SCHEME(14, "AI托管方案"),
    SUPSIM_EXPERT_SCHEME(15, "仿真推荐方案"),
    ALARM_RECOVERY_SCHEME(16, "报警恢复方案"),
    RAMP_LOCK_CONTROL(17, "匝道临时控制"),
    ;

    private Integer id;
    private String name;

    AlgorithmModeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static AlgorithmModeEnum getItem(Integer id) {
        for (AlgorithmModeEnum a : AlgorithmModeEnum.values()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return UNKNOWN;
    }

    public static String getName(Integer id) {
        AlgorithmModeEnum algorithmModeEnum = null;
        for (AlgorithmModeEnum s : AlgorithmModeEnum.values()) {
            if (s.getId().equals(id)) {
                algorithmModeEnum = s;
                break;
            }
        }
        return algorithmModeEnum == null ? null : algorithmModeEnum.getName();
    }

    public static Map<Integer, String> getAlgorithmMap() {
        Map<Integer, String> map = new HashMap<>();
        for (AlgorithmModeEnum e : AlgorithmModeEnum.values()) {
            map.put(e.getId(), e.getName());
        }
        return map;
    }

    @Override
    @JsonValue
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public SchemeTypeEnum convert2SchemeType() {
        switch (this) {
            case SCHEME:
                return SchemeTypeEnum.SUBAREA_ADAPTIVE;
            case ALARM_SCHEME:
                return SchemeTypeEnum.ALARM_HANDLING;
            case VARIABLE_LANE:
                return SchemeTypeEnum.CHANGEABLE_LANE;
            case SINGLE_ADAPTIVE:
                return SchemeTypeEnum.SINGLE_ADAPTIVE;
            case SINGLE_SMART:
                return SchemeTypeEnum.SINGLE_SMART;
            default:
                return null;
        }
    }
}
