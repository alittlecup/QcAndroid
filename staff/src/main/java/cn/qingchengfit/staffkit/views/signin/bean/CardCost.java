package cn.qingchengfit.staffkit.views.signin.bean;

import com.google.gson.annotations.SerializedName;

public class CardCost {
  @SerializedName(value = "cost") private float cost;

  public CardCost(float cost, int id) {
    this.cost = cost;
    this.id = id;
  }

  @SerializedName(value = "card_tpl_id") private int id;

  public float getCost() {
    return cost;
  }

  public void setCost(float cost) {
    this.cost = cost;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
