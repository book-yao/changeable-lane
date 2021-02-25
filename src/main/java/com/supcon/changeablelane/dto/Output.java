package com.supcon.changeablelane.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.supcon.changeablelane.constant.SchemeModeEnum;
import com.supcon.changeablelane.constant.SchemeSourceEnum;
import com.supcon.changeablelane.constant.SchemeTypeEnum;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import lombok.Data;

import java.util.List;

/**
 * @author JiangWangfa
 * @date 2018/4/22
 * @description 信号控制方案输出。算法相关的值对象。 每个子区均输出各自的信号控制方案，由信号控制系统下发到信号机执行
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Output {
    @JsonProperty("subAreaID")
    private Integer subAreaID;
    private Integer statusCode;
    private Integer keyAcsID;
    private List<AcsOutput> acsOutputs;
    /**
     * 锁定时长
     */
    private Integer lockTime;

    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String loadTime;

    /**
     * 算法厂商
     */
    private SchemeSourceEnum schemeSource;

    /**
     * 算法类型
     */
    private SchemeTypeEnum schemeType;
    /**
     * 适用场景
     */
    private SchemeModeEnum schemeMode;

    public String getCreatedTime() {
        return loadTime;
    }
}
