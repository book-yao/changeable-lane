package com.supcon.changeablelane.dto;

import com.supcon.changeablelane.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangfei
 * @date 2020/6/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

  /** 状态码 */
  private Integer code;

  /** 描述 */
  private String message;

  /** 数据 */
  private T data;

  public boolean isSuccess() {
    if (this.code == null) {
      return false;
    }

    return this.code.equals(StatusCode.CODE_SUCCESS);
  }

  public static ResponseDTO ofSuccess() {
    return ResponseDTO.builder().code(StatusCode.CODE_SUCCESS).build();
  }

  public static <T> ResponseDTO ofSuccess(T data) {
    return ResponseDTO.builder().code(StatusCode.CODE_SUCCESS).data(data).build();
  }

  public static <T> ResponseDTO ofSuccess(String message, T data) {
    return ResponseDTO.builder().code(StatusCode.CODE_SUCCESS).message(message).data(data).build();
  }

  public static <T> ResponseDTO ofSuccess(Integer code, String message, T data) {
    return ResponseDTO.builder().code(code).message(message).data(data).build();
  }

  /**
   * 参数异常
   *
   * @param statusCode
   * @param message
   * @return
   */
  public static ResponseDTO ofError(Integer statusCode, String message) {
    return ResponseDTO.builder().code(statusCode).message(message).build();
  }
}
