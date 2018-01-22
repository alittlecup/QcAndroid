package cn.qingchengfit.shop.vo;

import java.util.List;

/**
 * Created by huangbaole on 2018/1/19.
 */

public class Good {
  private int id;
  private int inventory;
  private String created_at;
  private String name;
  private List<Rule> rule;
  private Product product;

  public List<Rule> getRule() {
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

  public int getInventory() {
    return inventory;
  }

  public void setInventory(int inventory) {
    this.inventory = inventory;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public class Rule {
    private int cost;
    @Channel private String channel;

    public int getCost() {
      return cost;
    }

    public void setCost(int cost) {
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
