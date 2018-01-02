package cn.qingchengfit.saasbase.staff.beans.body;

import cn.qingchengfit.model.base.IBody;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2018/1/4.
 */

public class InvitationBody implements IBody{

  public String area_code;
  public String phone;
  public int gender;
  public String username;
  public String position_id;

  public InvitationBody() {
  }

  public String getArea_code() {
    return area_code;
  }

  public void setArea_code(String area_code) {
    this.area_code = area_code;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public int getGender() {
    return gender;
  }

  public void setGender(int gender) {
    this.gender = gender;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPosition_id() {
    return position_id;
  }

  public void setPosition_id(String position_id) {
    this.position_id = position_id;
  }

  private InvitationBody(Builder builder) {
    area_code = builder.area_code;
    phone = builder.phone;
    gender = builder.gender;
    username = builder.username;
    position_id = builder.position_id;
  }

  @Override public int check(int type) {
    return 0;
  }

  @Override public int check() {
    return 0;
  }

  public static final class Builder {
    private String area_code;
    private String phone;
    private int gender;
    private String username;
    private String position_id;

    public Builder() {
    }

    public Builder area_code(String val) {
      area_code = val;
      return this;
    }

    public Builder phone(String val) {
      phone = val;
      return this;
    }

    public Builder gender(int val) {
      gender = val;
      return this;
    }

    public Builder username(String val) {
      username = val;
      return this;
    }

    public Builder position_id(String val) {
      position_id = val;
      return this;
    }

    public InvitationBody build() {
      return new InvitationBody(this);
    }
  }
}
