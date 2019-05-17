package cn.qingchengfit.saasbase.turnovers;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.DateUtils;

public class TurOrderSellerHistory implements ITurOrderHistoryData {

  /**
   * created_at : 2018-12-09T14:40:16
   * id : 1
   * created_by : {"username":"邹英杰","gender":0,"id":545,"avatar":"https://img.qingchengfit.cn/977ad17699c4e4212b52000ed670091a.png!120x120"}
   * seller : {"username":"APP","gender":0,"id":3,"avatar":""}
   */

  private String created_at;
  private int id;
  private Staff created_by;
  private Staff seller;

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Staff getCreated_by() {
    return created_by;
  }

  public void setCreated_by(Staff created_by) {
    this.created_by = created_by;
  }

  public Staff getSeller() {
    return seller;
  }

  public void setSeller(Staff seller) {
    this.seller = seller;
  }

  @Override public String getDate() {
    return DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(created_at));
  }

  @Override public String getSellerName() {
    return created_by == null ? "" : created_by.getUsername();
  }

  @Override public String getChangeByName() {
    return seller == null ? "" : seller.getUsername();
  }
}
