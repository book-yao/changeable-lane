package com.supcon.changeablelane.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

/**
 * @author jiangfei
 * @date 2019/11/20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OSRequestDTO<T> {

  /** 当前版本号 */
  private String ver;

  /** 调用接口对应的用户名 */
  private String user;

  private String password;

  /** id */
  private String id;

  /** 开始查询起始序列 */
  private Integer index;

  /** 查询数量 */
  private Integer count;

  /** 下发方案 */
  private T data;

  /** 路口列表查询参数*/
  private T ar;

  /** 信号机历史周期数据*/
  private T acs_ids;

  public OSRequestDTO() {
    init();
  }

  public OSRequestDTO(Integer pageNo, Integer pageSize) {
    init();
    setPageNoAndPageSize(pageNo,pageSize);
  }

  public void setPageNoAndPageSize(Integer pageNo, Integer pageSize){
    this.index = pageNo + (pageNo - 1) * pageSize;
    this.count = pageSize;
  }

  public OSRequestDTO(String userName, String password) {
    ver = "1.0";
    this.user = userName;
    this.password = password;
  }

  private void init() {
    ver = "1.1";
    user = "sa";
    this.id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
  }
}
