package cn.qingchengfit.saasbase.student.network.body;

import cn.qingchengfit.model.base.QcStudentBean;

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
 * Created by Paper on 2017/8/28.
 */

public class EditStudentBody {

  public String username;
  public String date_of_birth;
  public String phone;
  public String address;
  public String joined_at;
  public String avatar;
  public String checkin_avatar;
  public Integer gender;
  public String seller_ids;
  public String coach_ids;
  public String shops;
  public String recommend_by_id;
  public String recommend_by;
  public String origin;
  public String remarks;
  public Integer status;
  public String area_code;


  private EditStudentBody(Builder builder) {
    username = builder.username;
    date_of_birth = builder.date_of_birth;
    phone = builder.phone;
    address = builder.address;
    joined_at = builder.joined_at;
    avatar = builder.avatar;
    checkin_avatar = builder.checkin_avatar;
    gender = builder.gender;
    seller_ids = builder.seller_ids;
    coach_ids = builder.coach_ids;
    shops = builder.shops;
    recommend_by_id = builder.recommend_by_id;
    recommend_by = builder.recommend_by;
    origin = builder.origin;
    remarks = builder.remarks;
    status = builder.status;
    area_code = builder.area_code;
  }

  public  static EditStudentBody instanteFromeQcStudent(QcStudentBean qc){
    return new Builder()
        .username(qc.getUsername())
        .area_code(qc.getArea_code())
        .avatar(qc.getAvatar())
        .phone(qc.getPhone())
        //.address(qc.)// TODO: 2017/8/28 地址
        //.remarks(qc.)// TODO: 2017/8/28 备注
        //.date_of_birth(qc.)// TODO: 2017/8/28 生日
        .build();
  }

  public static final class Builder {
    private String username;
    private String date_of_birth;
    private String phone;
    private String address;
    private String joined_at;
    private String avatar;
    private String checkin_avatar;
    private Integer gender;
    private String seller_ids;
    private String coach_ids;
    private String shops;
    private String recommend_by_id;
    private String recommend_by;
    private String origin;
    private String remarks;
    private Integer status;
    private String area_code;

    public Builder() {
    }

    public Builder username(String val) {
      username = val;
      return this;
    }

    public Builder date_of_birth(String val) {
      date_of_birth = val;
      return this;
    }

    public Builder phone(String val) {
      phone = val;
      return this;
    }

    public Builder address(String val) {
      address = val;
      return this;
    }

    public Builder joined_at(String val) {
      joined_at = val;
      return this;
    }

    public Builder avatar(String val) {
      avatar = val;
      return this;
    }

    public Builder checkin_avatar(String val) {
      checkin_avatar = val;
      return this;
    }

    public Builder gender(Integer val) {
      gender = val;
      return this;
    }

    public Builder seller_ids(String val) {
      seller_ids = val;
      return this;
    }

    public Builder coach_ids(String val) {
      coach_ids = val;
      return this;
    }

    public Builder shops(String val) {
      shops = val;
      return this;
    }

    public Builder recommend_by_id(String val) {
      recommend_by_id = val;
      return this;
    }

    public Builder recommend_by(String val) {
      recommend_by = val;
      return this;
    }

    public Builder origin(String val) {
      origin = val;
      return this;
    }

    public Builder remarks(String val) {
      remarks = val;
      return this;
    }

    public Builder status(Integer val) {
      status = val;
      return this;
    }

    public Builder area_code(String val) {
      area_code = val;
      return this;
    }

    public EditStudentBody build() {
      return new EditStudentBody(this);
    }
  }
}
