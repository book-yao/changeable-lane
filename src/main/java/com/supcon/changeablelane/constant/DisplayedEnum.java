package com.supcon.changeablelane.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 枚举获取id值
 *
 * @author JWF
 * @date 2019/7/17
 */
public interface DisplayedEnum {
  String DEFAULT_VALUE_NAME = "id";

  String DEFAULT_LABEL_NAME = "name";

  @JsonValue
  default Integer getId() {
    Field field = ReflectionUtils.findField(this.getClass(), DEFAULT_VALUE_NAME);
    if (field == null) {
      return null;
    }

    field.setAccessible(true);
    try {
      return Integer.parseInt(field.get(this).toString());
    } catch (IllegalAccessException e) {
      throw new RuntimeException("cannot get default value:[" + DEFAULT_VALUE_NAME + "]", e);
    }
  }

  default String getName() {
    Field field = ReflectionUtils.findField(this.getClass(), DEFAULT_LABEL_NAME);
    if (field == null) return null;
    try {
      field.setAccessible(true);
      return field.get(this).toString();
    } catch (IllegalAccessException e) {
      throw new RuntimeException("cannot get default value:[" + DEFAULT_LABEL_NAME + "]", e);
    }
  }

  default <E extends Enum<E>> E getItem(Class<E> enumClass, Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("id should not be null");
    }

    if (enumClass.isAssignableFrom(DisplayedEnum.class)) {
      throw new IllegalArgumentException("illegal DisplayedEnum type");
    }

    E[] enums = enumClass.getEnumConstants();
    for (E e : enums) {
      DisplayedEnum displayedEnum = (DisplayedEnum) e;
      if (displayedEnum.getId().equals(id)) {
        return (E) displayedEnum;
      }
    }

    throw new IllegalArgumentException(
        "convert id:" + id + "to " + enumClass.getName() + "failed.");
  }
}
