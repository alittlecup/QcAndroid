package cn.qingchengfit.pos.login.model;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginBody {

  public String phone;
  public String code;
  public String area_code;

  public LoginBody(String phone, String code, String area_code) {
    this.phone = phone;
    this.code = code;
    this.area_code = area_code;
  }
}
