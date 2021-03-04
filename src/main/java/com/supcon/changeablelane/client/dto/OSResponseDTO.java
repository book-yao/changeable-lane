package com.supcon.changeablelane.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author jiangfei
 * @date 2019/11/20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OSResponseDTO<T> {

    /**
     * id
     */
    private String id;

    /**
     * 请求成功时，Code为0，Data部分是单个json对象
     */
    private Integer code;

    /**
     * 错误描述信息
     */
    private String desc;

    /**
     * 请求失败时，Code非0，Data部分是错误描述字符串
     */
    private T data;
}
