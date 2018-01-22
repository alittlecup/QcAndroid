package cn.qingchengfit.shop.vo;

import java.util.List;

/**
 * Created by huangbaole on 2018/1/22.
 */

public class Record {
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
  private String created_at;
  private int inventory;
  private CreateBy created_by;
  private List<Good> goods;
  private String total_inventory;
  private List<Stat> stat;

  private class Stat {
    private String name;
    private String inventory;
  }
}
