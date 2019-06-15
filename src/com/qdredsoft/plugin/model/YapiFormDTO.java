package com.qdredsoft.plugin.model;

import java.io.Serializable;

/**
 * query
 *
 * @date 2019/2/11 5:05 PM
 */
public class YapiFormDTO extends ValueWraper implements Serializable {

  private String _id;

  /**
   * 类型 text or file
   */
  private String type = "text";

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public YapiFormDTO() {
  }
}
