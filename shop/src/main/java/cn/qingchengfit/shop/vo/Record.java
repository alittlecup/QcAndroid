package cn.qingchengfit.shop.vo;

import cn.qingchengfit.shop.ui.items.inventory.IInventoryRecordData;

/**
 * Created by huangbaole on 2018/1/22.
 */

public class Record implements IInventoryRecordData{
  /**
   * "records": [
   * {
   * id: 10,
   * offset: 10,
   * action_type: 1增加 2减少 3售卖 4退货,
   * created_at: "",
   * inventory: 10,
   * created_by: {
   * id: 10,
   * username: "",
   * },
   * goods: {
   * id: 10,
   * name: "",
   * product: {
   * "id": 1,
   * name: "",
   * unit: "",
   * }
   * }
   * }],
   * "stat": [
   * {
   * "name": xxx,
   * "inventory": 10,
   * }
   * ]
   * ,
   * total_inventory:""}
   */
  private int id;
  private int offset;
  @RecordAction private int action_type;
  private int inventory;
  private String created_at;
  private CreateBy created_by;
  //private List<Good> goods;
  private int goods_id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override public String getProductName() {
    return "水";
  }

  @Override public String getGoodName() {
    return "大";
  }

  @Override public String getCreateName() {
    return created_by.getUsername();
  }

  @Override public String getCreateTime() {
    return created_at;
  }

  @Override public int getOperatorType() {
    return action_type;
  }

  public int getOffset() {
    return offset;
  }

  @Override public int getInventorCount() {
    return inventory;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getAction_type() {
    return action_type;
  }

  public void setAction_type(int action_type) {
    this.action_type = action_type;
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

  public CreateBy getCreated_by() {
    return created_by;
  }

  public void setCreated_by(CreateBy created_by) {
    this.created_by = created_by;
  }

  //public List<Good> getGoods() {
  //  return goods;
  //}
  //
  //public void setGoods(List<Good> goods) {
  //  this.goods = goods;
  //}

  public int getGoods_id() {
    return goods_id;
  }

  public void setGoods_id(int goods_id) {
    this.goods_id = goods_id;
  }

  public class Stat {
    private String name;
    private String inventory;

    public void setName(String name) {
    }

    public void setInventory(String inventory) {
      this.inventory = inventory;
    }

    public String getName() {
      return name;
    }

    public String getInventory() {
      return inventory;
    }
  }
}
