package cn.qingchengfit.writeoff.vo.impl;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.writeoff.vo.ITicketDetailData;

public class Ticket implements Parcelable, ITicketDetailData {
  private String id;
  private String title;//电子券标题(名称)
  private float price;//电子券金额，单位(元)，
  private int status;//1-已核销 2- 未核销
  private String e_code;//核销码
  private String start;//生效时间
  private String end; //失效时间
  private String created_at;//购买时间
  private String out_trade_no;//订单编号
  private int used_type;//0-未知  1-团课 2- 私教
  private String used_at;//上课时间
  private String verify_at;//核销时间
  private String shop_name;
  private TicketInnerTempObject shop;
  private TicketInnerTempObject course;
  private TicketInnerTempObject teacher;
  private String username;
  private String phone;
  private int gender;
  private String area_code;

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setE_code(String e_code) {
    this.e_code = e_code;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public void setUsed_type(int used_type) {
    this.used_type = used_type;
  }

  public void setUsed_at(String used_at) {
    this.used_at = used_at;
  }


  public void setShop(TicketInnerTempObject shop) {
    this.shop = shop;
  }

  public void setCourse(TicketInnerTempObject course) {
    this.course = course;
  }

  public void setTeacher(TicketInnerTempObject teacher) {
    this.teacher = teacher;
  }

  public Ticket() {
  }

  @Override public String getTicketNumber() {
    return e_code;
  }

  @Override public String getTicketStatus() {
    return status == 1 ? "已核销" : "未核销";
  }

  @Override public String getTicketStartEnd() {
    return DateUtils.getYYYYMMDDfromServer(start).replace("-", ".")
        + "-"
        + (DateUtils.formatDateToServer(DateUtils.getYYYYMMDDfromServer(end)).replace("-", "."));
  }

  @Override public String getTicketBuyTime() {
    return DateUtils.Date2YYYYMMDDHHmmss(DateUtils.formatDateFromServer(created_at));
  }

  @Override public String getTicketGymName() {
    if (shop != null) {
      return shop.name;
    }
    return shop_name;
  }

  @Override public String getTicketBatchType() {
    switch (used_type) {
      case 1:
        return "团课";
      case 2:
        return "私教";
      default:
        return "未知";
    }
  }

  @Override public String getTicketBatch() {
    if (course == null) return "";
    return course.name;
  }

  @Override public String getTicketBatchTrainer() {
    if (teacher == null) return "";
    return teacher.name;
  }



  @Override public String getTicketBatchTime() {
    return DateUtils.Date2YYYYMMDDHHmmss(DateUtils.formatDateFromServer(used_at));
  }

  @Override public String getTickerUserNanme() {
    return username;
  }

  @Override public String getTickerUserGender() {
    return gender == 1 ? "女" : "男";
  }

  @Override public String getTickerUserPhone() {
    return phone;
  }

  @Override public String getTickerUserPhoneArea() {
    return area_code;
  }

  @Override public boolean isTicketChecked() {
    return status == 1;
  }

  @Override public String getTicketName() {
    return title;
  }

  @Override public String getTicketCheckTime() {
    return DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(verify_at));
  }

  @Override public String getTicketMoney() {
    return String.valueOf(price);
  }

  @Override public String getTickerId() {
    return id;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.title);
    dest.writeFloat(this.price);
    dest.writeInt(this.status);
    dest.writeString(this.e_code);
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeString(this.created_at);
    dest.writeString(this.out_trade_no);
    dest.writeInt(this.used_type);
    dest.writeString(this.used_at);
    dest.writeString(this.verify_at);
    dest.writeString(this.shop_name);
    dest.writeParcelable(this.shop, flags);
    dest.writeParcelable(this.course, flags);
    dest.writeParcelable(this.teacher, flags);
    dest.writeString(this.username);
    dest.writeString(this.phone);
    dest.writeInt(this.gender);
    dest.writeString(this.area_code);
  }

  protected Ticket(Parcel in) {
    this.id = in.readString();
    this.title = in.readString();
    this.price = in.readFloat();
    this.status = in.readInt();
    this.e_code = in.readString();
    this.start = in.readString();
    this.end = in.readString();
    this.created_at = in.readString();
    this.out_trade_no = in.readString();
    this.used_type = in.readInt();
    this.used_at = in.readString();
    this.verify_at = in.readString();
    this.shop_name = in.readString();
    this.shop = in.readParcelable(TicketInnerTempObject.class.getClassLoader());
    this.course = in.readParcelable(TicketInnerTempObject.class.getClassLoader());
    this.teacher = in.readParcelable(TicketInnerTempObject.class.getClassLoader());
    this.username = in.readString();
    this.phone = in.readString();
    this.gender = in.readInt();
    this.area_code = in.readString();
  }

  public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
    @Override public Ticket createFromParcel(Parcel source) {
      return new Ticket(source);
    }

    @Override public Ticket[] newArray(int size) {
      return new Ticket[size];
    }
  };
}