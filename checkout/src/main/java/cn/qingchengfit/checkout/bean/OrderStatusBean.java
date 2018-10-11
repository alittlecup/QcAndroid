package cn.qingchengfit.checkout.bean;

public class OrderStatusBean {
  private String pay_trade_no = "";

  public String getPay_trade_no() {
    return pay_trade_no;
  }

  public void setPay_trade_no(String pay_trade_no) {
    this.pay_trade_no = pay_trade_no;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getSuccess_url() {
    return success_url;
  }

  public void setSuccess_url(String success_url) {
    this.success_url = success_url;
  }

  public String getFail_url() {
    return fail_url;
  }

  public void setFail_url(String fail_url) {
    this.fail_url = fail_url;
  }

  private int status;
  private String success_url = "";
  private String fail_url = "";
}
