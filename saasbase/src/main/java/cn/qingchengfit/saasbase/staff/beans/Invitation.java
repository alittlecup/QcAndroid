package cn.qingchengfit.saasbase.staff.beans;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;

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

public class Invitation implements ICommonUser, Parcelable {
  private String uuid;
  private int status;
  private String area_code;
  private String phone;
  private int gender;
  private String username;
  private StaffPosition position;
  private String url;
  private String qrcode;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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

  @Override public String getAvatar() {
    return "";
  }

  @Override public String getTitle() {
    return username;
  }

  @Override public String getSubTitle() {
    return phone;
  }

  @Override public String getContent() {
    if (position != null)
      return position.getName();
    return "";
  }

  @Override public String getId() {
    return uuid;
  }

  @Override public String getRight() {
    if (status == 1){
      return "邀请中";
    }else if (status == 2){
      return "已邀请";
    }else return "已撤销";
  }

  @Override public int getRightColor() {
    if (status == 1){
      return R.color.red;
    }else if (status == 2){
      return R.color.btn_text_primary_color;
    }else return R.color.text_grey;
  }

  public int getGender() {
    return gender;
  }

  @Override public boolean filter(String str) {
    return phone.contains(str) || username.contains(str);
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

  public StaffPosition getPosition() {
    return position;
  }

  public void setPosition(StaffPosition position) {
    this.position = position;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getQrcode() {
    return qrcode;
  }

  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.uuid);
    dest.writeInt(this.status);
    dest.writeString(this.area_code);
    dest.writeString(this.phone);
    dest.writeInt(this.gender);
    dest.writeString(this.username);
    dest.writeParcelable(this.position, flags);
    dest.writeString(this.url);
    dest.writeString(this.qrcode);
  }

  public Invitation() {
  }

  protected Invitation(Parcel in) {
    this.uuid = in.readString();
    this.status = in.readInt();
    this.area_code = in.readString();
    this.phone = in.readString();
    this.gender = in.readInt();
    this.username = in.readString();
    this.position = in.readParcelable(StaffPosition.class.getClassLoader());
    this.url = in.readString();
    this.qrcode = in.readString();
  }

  public static final Parcelable.Creator<Invitation> CREATOR =
    new Parcelable.Creator<Invitation>() {
      @Override public Invitation createFromParcel(Parcel source) {
        return new Invitation(source);
      }

      @Override public Invitation[] newArray(int size) {
        return new Invitation[size];
      }
    };
}
