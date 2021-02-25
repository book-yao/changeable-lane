package com.supcon.changeablelane.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 算法类型
 *
 * @author lidewen
 * @date 2019/4/30
 */
public enum SchemeTypeEnum {
  UNKNOWN(0, "未知"),
  SUBAREA_ADAPTIVE(1, "子区自适应算法"),
  ALARM_HANDLING(2, "报警处置算法"),
  CHANGEABLE_LANE(3, "可变车道算法"),
  SINGLE_ADAPTIVE(4, "单点自适应算法"),
  SINGLE_SMART(5, "单点知识库算法"),
  SPLIT_ALGORITHM(6, "绿信比算法");

  /** id */
  private Integer id;

  /** 名称 */
  private String name;

  SchemeTypeEnum(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  @JsonCreator
  public static SchemeTypeEnum getItem(Integer id) {
    for (SchemeTypeEnum item : values()) {
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
