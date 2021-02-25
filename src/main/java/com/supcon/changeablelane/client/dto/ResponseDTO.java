package com.supcon.changeablelane.client.dto;

import lombok.Data;

/**
 * @Author caowenbo
 * @create 2021/2/25 19:26
 */
@Data
public class ResponseDTO<T> {
    private int code;

    private T message;
}
