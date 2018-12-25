package cn.qingchengfit.saasbase.turnovers;

import com.google.gson.annotations.SerializedName;

public class TurnoversChartStatData {

  /**
   * growth_rate : null
   * amount : 0
   * trade_type : 1
   * growth_value : 0
   */

  private float growth_rate;
  @SerializedName(value = "amount",alternate = {"total_amount"})
  private float amount;
  private int trade_type;
  private float growth_value;

  public float getGrowth_rate() {
    return growth_rate;
  }

  public void setGrowth_rate(float growth_rate) {
    this.growth_rate = growth_rate;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public int getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(int trade_type) {
    this.trade_type = trade_type;
  }

  public float getGrowth_value() {
    return growth_value;
  }

  public void setGrowth_value(float growth_value) {
    this.growth_value = growth_value;
  }
}
