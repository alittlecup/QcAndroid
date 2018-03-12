package cn.qingchengfit.shop.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.shop.BR;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/19.
 */

public class Good extends BaseObservable implements Parcelable {
  private String id;
  private Integer inventory;
  private String created_at;
  private String name;
  private List<Rule> rule;

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  private Product product;

  public List<Rule> getRule() {
    if (rule == null) {
      rule = new ArrayList<>();
    }
    return rule;
  }

  public void setRule(List<Rule> rule) {
    this.rule = rule;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getInventory() {
    return inventory;
  }

  @Bindable public String getInventoryStr() {
    return String.valueOf(inventory == null ? "" : inventory);
  }

  public void setInventory(Integer inventory) {
    this.inventory = inventory;
  }

  public void setInventoryStr(String inventory) {
    if (StringUtils.isEmpty(inventory)) return;
    if (!StringUtils.isEmpty(inventory)) {
      try {
        if (getInventoryStr().equals(inventory)) return;
        this.inventory = Integer.valueOf(inventory);
        if (this.inventory > 9999) {
          this.inventory =
              Integer.valueOf(inventory.subSequence(0, inventory.length() - 1).toString());
          ToastUtils.show("库存最多为9999");
        }
      } catch (NumberFormatException ex) {
        this.inventory =
            Integer.valueOf(inventory.subSequence(0, inventory.length() - 1).toString());
        ToastUtils.show("请输入正确整数库存");
      }
      notifyPropertyChanged(BR.inventoryStr);
    }
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  @Bindable public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    notifyPropertyChanged(BR.name);
  }

  public String getPrice(@Channel String type) {
    if (rule != null && !rule.isEmpty()) {
      for (Rule rule : rule) {
        if (type.equals(rule.getChannel())) {
          return rule.getCost();
        }
      }
    }
    return "";
  }

  @Bindable public String getCardPrices() {
    return getPrice(Channel.CARD);
  }

  @Bindable public String getRmbPrices() {
    return getPrice(Channel.RMB);
  }

  public void setPrice(String price, @Channel String type) {
    if (rule != null && !rule.isEmpty()) {
      for (Rule rule : rule) {
        if (type.equals(rule.getChannel())) {
          rule.setCost(price);
          return;
        }
      }
    }
    if (rule == null) rule = new ArrayList<>();
    Rule rule = new Rule();
    rule.setCost(price);
    rule.setChannel(type);
    this.rule.add(rule);
  }

  public void removeCardPrice() {
    if (rule != null && !rule.isEmpty()) {
      for (Rule rule : rule) {
        if (rule.getChannel().equals(Channel.CARD)) {
          this.rule.remove(rule);
        }
      }
    }
  }

  public static class Rule {

    protected String cost;

    @Channel private String channel;

    public String getCost() {
      return cost;
    }

    public void setCost(String cost) {
      this.cost = cost;
    }

    public String getChannel() {
      return channel;
    }

    public void setChannel(String channel) {
      this.channel = channel;
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeValue(this.inventory);
    dest.writeString(this.created_at);
    dest.writeString(this.name);
    dest.writeList(this.rule);
    dest.writeParcelable(this.product, flags);
  }

  public Good() {
  }

  protected Good(Parcel in) {
    this.id = in.readString();
    this.inventory = (Integer) in.readValue(Integer.class.getClassLoader());
    this.created_at = in.readString();
    this.name = in.readString();
    this.rule = new ArrayList<Rule>();
    in.readList(this.rule, Rule.class.getClassLoader());
    this.product = in.readParcelable(Product.class.getClassLoader());
  }

  public static final Parcelable.Creator<Good> CREATOR = new Parcelable.Creator<Good>() {
    @Override public Good createFromParcel(Parcel source) {
      return new Good(source);
    }

    @Override public Good[] newArray(int size) {
      return new Good[size];
    }
  };
}
