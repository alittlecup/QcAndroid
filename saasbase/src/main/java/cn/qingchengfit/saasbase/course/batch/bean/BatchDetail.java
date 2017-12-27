package cn.qingchengfit.saasbase.course.batch.bean;

import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class BatchDetail {
  @SerializedName("id") public String id;
  @SerializedName("shop") public Shop shop;
  @SerializedName("max_users") public int max_users;
  @SerializedName("from_date") public String from_date;
  @SerializedName("to_date") public String to_date;
  @SerializedName("spaces") public List<Space> spaces;
  public Space space;
  //public boolean is_open_for_bodys;
  @SerializedName("course") public BatchCourse course;
  @SerializedName(value = "rule",alternate = {"rules"}) public List<Rule> rule;
  @SerializedName("is_free") public boolean is_free;
  @SerializedName("teacher") public Staff teacher;
  @SerializedName("time_repeats") public List<Time_repeat> time_repeats;
  public List<CardTplBatchShip> card_tpls;
  public boolean has_order;
  public BatchOpenRule open_rule;

  public List<Space> getSpaces() {
    if (spaces != null) {
      return spaces;
    } else {
      List<Space> spaces = new ArrayList<>();
      if (space != null) spaces.add(space);
      return spaces;
    }
  }

  public boolean supportMulti() {
    if (course != null && course.is_private){
      return max_users > 1;
    }else {
      return hasCardTplid(rule);
    }

  }

  /**
   * 判断多人团课
   * @param rules
   * @return
   */
  private boolean hasCardTplid(List<Rule> rules){
    if (rules != null){
      List<String> strs = new ArrayList<>();
      for (Rule rule1 : rules) {
        if (strs.contains(rule1.card_tpl_id))
          return true;
        else strs.add(rule1.card_tpl_id);
      }
      return false;
    }else return false;
  }
}