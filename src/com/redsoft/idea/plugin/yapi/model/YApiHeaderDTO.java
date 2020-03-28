package com.redsoft.idea.plugin.yapi.model;

/**
 * header
 *
 * @date 2019/5/9 10:11 PM
 */
@SuppressWarnings("unused")
public class YApiHeaderDTO extends ValueWrapper {

  /**
   * 值
   */
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
