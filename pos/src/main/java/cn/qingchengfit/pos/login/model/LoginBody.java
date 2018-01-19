package cn.qingchengfit.pos.login.model;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginBody {

  public String phone;
  public String code;
  public String area_code;
  public String gym_id;

  public LoginBody(Builder builder) {
    this.phone = builder.phone;
    this.code = builder.code;
    this.area_code = builder.area_code;
    this.gym_id = builder.gym_id;
  }

  public static final class Builder{
    private String phone;
    private String code;
    private String area_code;
    private String gym_id;

    public Builder() {
    }

    public Builder phone(String val) {
      phone = val;
      return this;
    }

    public Builder code(String val) {
      code = val;
      return this;
    }

    public Builder area_code(String val) {
      area_code = val;
      return this;
    }

    public Builder gym_id(String val) {
      gym_id = val;
      return this;
    }

    public LoginBody build() {
      return new LoginBody(this);
    }

  }

}
