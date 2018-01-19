package cn.qingchengfit.pos.login.model;

/**
 * Created by fb on 2017/10/19.
 */

public class GetCodeBody {
  public String phone;
  public String area_code;
  public String gym_id;
  public String code;

  public GetCodeBody(Builder builder) {
    this.phone = builder.phone;
    this.area_code = builder.area_code;
    this.code = builder.code;
    this.gym_id = builder.gym_id;
  }

  public static final class Builder{
    private String phone;
    private String code;
    private String area_code;
    private String gym_id;

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

    public GetCodeBody build() {
      return new GetCodeBody(this);
    }
  }

}
