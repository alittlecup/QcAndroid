package cn.qingchengfit.shop.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.shop.BR;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/19.
 */

public class Good extends BaseObservable implements Parcelable {
  private String id;
  private Long inventory;
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

  public Long getInventory() {
    return inventory;
  }

  @Bindable public String getInventoryStr() {
    return String.valueOf(inventory == null ? "" : inventory);
  }

  public void setInventory(Long inventory) {
    this.inventory = inventory;
  }

  public void setInventoryStr(String inventory) {
    if (StringUtils.isEmpty(inventory)) return;
    if (!StringUtils.isEmpty(inventory)) {
      try {
        if (getInventoryStr().equals(inventory)) return;
        this.inventory = Long.valueOf(inventory);
        if (this.inventory > 9999) {
          this.inventory =
              Long.valueOf(inventory.subSequence(0, inventory.length() - 1).toString());
          ToastUtils.show("库存数量不能大于9999");
        }
      } catch (NumberFormatException ex) {
        this.inventory = Long.valueOf(inventory.subSequence(0, inventory.length() - 1).toString());
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
    if (this.name != null && this.name.equals(name)) {
      return;
    }
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

  public static class Rule implements Parcelable {

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

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.cost);
      dest.writeString(this.channel);
    }

    public Rule() {
    }

    protected Rule(Parcel in) {
      this.cost = in.readString();
      this.channel = in.readString();
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
      @Override public Rule createFromParcel(Parcel source) {
        return new Rule(source);
      }

      @Override public Rule[] newArray(int size) {
        return new Rule[size];
      }
    };
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
    this.inventory = (Long) in.readValue(Long.class.getClassLoader());
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
