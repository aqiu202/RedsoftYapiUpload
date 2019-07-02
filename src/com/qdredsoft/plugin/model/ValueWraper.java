package com.qdredsoft.plugin.model;

import java.util.Objects;

public class ValueWraper {

  /**
   * 是否必填
   */
  protected String required = "1";

  /**
   * 描述
   */
  protected String desc;
  /**
   * 示例
   */
  protected String example;
  /**
   * 参数名字
   */
  protected String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public String getExample() {
    return example;
  }

  public void setExample(String example) {
    this.example = example;
  }

  public void full(ValueWraper valueWraper) {
    if(Objects.nonNull(valueWraper.getName())){
      this.setName(valueWraper.getName());
    }
    if(Objects.nonNull(valueWraper.getDesc())){
      this.setDesc(valueWraper.getDesc());
    }
    if(Objects.nonNull(valueWraper.getRequired())){
      this.setRequired(valueWraper.getRequired());
    }
    if(Objects.nonNull(valueWraper.getExample())){
      this.setExample(valueWraper.getExample());
    }
  }

  @Override
  public boolean equals(Object o) {
    if( o == null || !(o instanceof ValueWraper)) {
      return false;
    }
    ValueWraper target = (ValueWraper) o;
    return this.name.equals(target.getName());
  }

  @Override
  public int hashCode(){
    return Objects.hash(this.name);
  }
}
