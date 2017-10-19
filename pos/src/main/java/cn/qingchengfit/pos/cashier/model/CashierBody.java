package cn.qingchengfit.pos.cashier.model;

/**
 * Created by fb on 2017/10/19.
 */

public class CashierBody {

  public String username;
  public int gender;
  public String gym_id;
  public String phone;

  public CashierBody(Builder builder) {
    this.username = builder.username;
    this.gender = builder.gender;
    this.gym_id = builder.gym_id;
    this.phone = builder.phone;
  }

  public static final class Builder{
    private String username;
    private int gender;
    private String gym_id;
    private String phone;

    public Builder phone(String val) {
      phone = val;
      return this;
    }

    public Builder username(String val) {
      username = val;
      return this;
    }

    public Builder gender(int val) {
      gender = val;
      return this;
    }

    public Builder gym_id(String val) {
      gym_id = val;
      return this;
    }

    public CashierBody build(){
      return new CashierBody(this);
    }

  }

}
