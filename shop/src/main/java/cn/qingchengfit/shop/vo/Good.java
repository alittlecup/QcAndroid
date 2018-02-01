package cn.qingchengfit.shop.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.shop.BR;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/19.
 */

public class Good extends BaseObservable {
  private Integer id;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
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
    this.inventory = Integer.valueOf(inventory);
    notifyPropertyChanged(BR.inventoryStr);
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

  public double getPrice(@Channel String type) {
    if (rule != null && !rule.isEmpty()) {
      for (Rule rule : rule) {
        if (type.equals(rule.getChannel())) {
          return rule.getCost();
        }
      }
    }
    return -1;
  }

  @Bindable public String getCardPrices() {
    return tempCardPrice;
  }

  public void setCardPrices(String prices) {
    if (StringUtils.isEmpty(prices)) {
      tempCardPrice = prices;
      return;
    }
    if (".".equals(prices)) {
      tempCardPrice = "";
    }
    int i = prices.lastIndexOf(".");
    if (i != -1 && prices.length() - 1 >= 4) {
      tempCardPrice = prices.substring(0, i + 2);
    } else {
      tempCardPrice = prices;
    }
    notifyPropertyChanged(BR.cardPrices);
  }

  @Bindable public String getRmbPrices() {
    return tempRMBPrice;
  }

  private String tempRMBPrice;
  private String tempCardPrice;

  public void setRmbPrices(String prices) {
    if (StringUtils.isEmpty(prices)) return;
    notifyPropertyChanged(BR.rmbPrices);
  }

  public void setPrice(double price, @Channel String type) {
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

  public static class Rule {

    protected double cost;

    @Channel private String channel;

    public double getCost() {
      return cost;
    }

    public void setCost(double cost) {
      this.cost = cost;
    }

    public String getChannel() {
      return channel;
    }

    public void setChannel(String channel) {
      this.channel = channel;
    }
  }
}
