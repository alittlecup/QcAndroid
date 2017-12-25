package cn.qingchengfit.saasbase.course.batch.network.body;

import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.ArrayList;
import java.util.List;

public class SingleBatchBody {
  public String id;
  public String model;
  public String start;
  public String end;

  public String teacher_id;
  public String course_id;
  public String shop_id;
  public String from_date;
  public String to_date;
  public List<String> spaces;
  public ArrayList<Rule> rule;
  public boolean is_free;
  public int max_users;
  public BatchOpenRule open_rule = new BatchOpenRule();

  public int check() {
    if (CmStringUtils.isEmpty(teacher_id))
      return R.string.err_no_trainer;
    if (!CmStringUtils.isID(course_id))
      return R.string.err_no_course;
    if (CmStringUtils.isEmpty(start))
      return R.string.err_no_start_date;
    if (max_users <= 0)
      return R.string.err_max_user;
    if (spaces == null || spaces.size() == 0)
      return R.string.err_no_space;
    if (!is_free &&(rule == null || rule.size() == 0))
      return R.string.err_no_rule;
    if (open_rule == null)
      return R.string.err_no_openrule;
    return 0;
  }
}