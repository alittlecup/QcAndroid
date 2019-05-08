package cn.qingchengfit.saascommon.filter.model;

/**
 * Created by fb on 2017/10/17.
 */

public class Content {

  public String name;
  public String value;
  public UserExtra extra;

  public Content(){

  }

  public Content(String name, String value, UserExtra extra) {
    this.name = name;
    this.value = value;
    this.extra = extra;
  }
}
