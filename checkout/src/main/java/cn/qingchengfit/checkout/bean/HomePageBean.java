package cn.qingchengfit.checkout.bean;

import com.google.gson.annotations.SerializedName;

/**
 * GET: api/staffs/:staff_id/cashier/stat/
 * {
 * sum: 1000.00
 * new: 10,
 * charge: 10,
 * cashier: 10,
 * }
 */
public class HomePageBean {
  @SerializedName("new") private int new_count;
  private int charge;
  private int cashier;
  private String sum;

  public int getNew_count() {
    return new_count;
  }

  public void setNew_count(int new_count) {
    this.new_count = new_count;
  }

  public int getCharge() {
    return charge;
  }

  public void setCharge(int charge) {
    this.charge = charge;
  }

  public int getCashier() {
    return cashier;
  }

  public void setCashier(int cashier) {
    this.cashier = cashier;
  }

  public String getSum() {
    return sum;
  }

  public void setSum(String sum) {
    this.sum = sum;
  }

}
